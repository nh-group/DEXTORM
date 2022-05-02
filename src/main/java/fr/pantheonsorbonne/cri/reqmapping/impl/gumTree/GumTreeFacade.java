package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeVisitor;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor.CompilationUnitVisitor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GumTreeFacade {

    public static final String BLAME_ID = "blameid";
    private List<Diff> diffs;

    {
        Arrays.asList(JavaParserGenerator.class).forEach(gen -> {
            com.github.gumtreediff.gen.Register a = gen.getAnnotation(com.github.gumtreediff.gen.Register.class);
            if (a != null)
                TreeGenerators.getInstance().install(gen, a);
        });
    }


    @SuppressWarnings("unchecked")
    public static void appendMetadata(Tree t, String key, Object value, boolean recursive) {

        Collection<Object> existingValue = (Collection<Object>) t.getMetadata(key);
        if (existingValue == null) {
            existingValue = Sets.newHashSet();
            t.setMetadata(key, existingValue);
        }

        if (value instanceof Collection) {
            existingValue.addAll((Collection<Object>) value);
        } else if (value instanceof String && !Strings.isNullOrEmpty(((String) value))) {
            existingValue.add(value);
        }


        for (Tree tt : t.getChildren()) {
            appendMetadata(tt, key, value, recursive);
        }


    }

    public static void addCommitMetadataToTreeRecursive(Tree t, String commitID) {
        GumTreeFacade.appendMetadata(t, BLAME_ID, commitID, true);
    }

    public static void addCommitMetadataToTree(Tree t, String commitID) {
        GumTreeFacade.appendMetadata(t, BLAME_ID, commitID, false);
    }

    private static Set<ReqMatch> getReqMatcher(final TreeContext ctx) {

        CompilationUnitVisitor visitor = new CompilationUnitVisitor(ctx, ReqMatch.newBuilder());
        TreeVisitor.visitTree(ctx.getRoot(), visitor);

        return visitor.getMatchers().stream().map(ReqMatcherBuilder::build).collect(Collectors.toSet());

    }

    public void labelDestWithCommit(DiffTree d) {

        Matcher m = Matchers.getInstance().getMatcher(); // retrieve the default matcher
        MappingStore store = m.match(d.src, d.dst);


        for (Tree t : d.dst.getChildren()) {

            if (store.getSrcForDst(t) != null) {
                GumTreeFacade.appendMetadata(t, BLAME_ID, store.getSrcForDst(t).getMetadata(BLAME_ID), false);
            } else {
                GumTreeFacade.appendMetadata(t, BLAME_ID, d.commitId, false);
            }

        }
    }

    public Collection<ReqMatch> getReqMatcher(List<Diff> diffs) {

        TreeContext currentContext = null;

        for (Diff diff : diffs) {

            DiffTree currentDiff;
            try {
                currentDiff = diff.toDiffTree();

                if (currentContext == null) {
                    GumTreeFacade.appendMetadata(currentDiff.dst, GumTreeFacade.BLAME_ID,
                            currentDiff.commitId, true);
                } else {
                    currentDiff.src = currentContext.getRoot();
                    this.labelDestWithCommit(currentDiff);
                }
                currentContext = new TreeContext();
                currentContext.setRoot(currentDiff.dst);
            } catch (UnsupportedOperationException | IOException e) {

                e.printStackTrace();
                System.exit(-2);
            }

        }

        return GumTreeFacade.getReqMatcher(currentContext);

    }

}
