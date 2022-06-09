package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import com.google.common.base.Strings;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatchImpl;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor.CompilationUnitVisitor;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GumTreeFacade {

    public static final String BLAME_ID = "blameid";


    {
        Arrays.asList(JavaParserGenerator.class).forEach(gen -> {
            com.github.gumtreediff.gen.Register a = gen.getAnnotation(com.github.gumtreediff.gen.Register.class);
            if (a != null)
                TreeGenerators.getInstance().install(gen, a);
        });
    }

    @SuppressWarnings("unchecked")
    public static void appendMetadata(Tree t, String key, Object value, boolean recursive) {

        List<Object> existingValue = (List<Object>) t.getMetadata(key);
        if (existingValue == null) {
            existingValue = new ArrayList<>();
            t.setMetadata(key, existingValue);
        }

        if (value instanceof Collection) {
            existingValue.addAll((List<Object>) value);
        } else if (value instanceof String && !Strings.isNullOrEmpty(((String) value))) {
            existingValue.add(value);
        }

        if (recursive) {
            for (Tree tt : t.getChildren()) {
                appendMetadata(tt, key, value, true);
            }
        }


    }

    public static void addCommitMetadataToTreeRecursive(Tree t, String commitID) {
        GumTreeFacade.appendMetadata(t, BLAME_ID, commitID, true);
    }

    public static void addCommitMetadataToTree(Tree t, String commitID) {
        GumTreeFacade.appendMetadata(t, BLAME_ID, commitID, false);
    }

    private static List<ReqMatch> getReqMatcher(final Tree tree, boolean doMethods, boolean doInstructions) {

        CompilationUnitVisitor visitor = new CompilationUnitVisitor(tree, ReqMatchImpl.newBuilder(), 0, doMethods, doInstructions);
        TreeVisitor.visitTree(tree, visitor);

        return visitor.getMatchersBuilders().stream().map(ReqMatcherBuilder::build).collect(Collectors.toList());

    }

    public static void showTree(Tree father, String offset, PrintStream ps) {
        String metadata = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        father.getMetadata(),
                        Spliterator.ORDERED)
                , false).map(e -> e.getKey() + ":" + e.getValue().toString()).collect(Collectors.joining(" "));
        ps.println(offset + " " + father.getType().toString() + " " + father.getLabel() + " " + metadata);
        for (Tree t : father.getChildren()) {
            showTree(t, offset + "\t", ps);
        }
    }

    public void labelDestWithCommit(Tree src, Tree dst, String commitId) {

        Matcher m = Matchers.getInstance().getMatcher(); // retrieve the default matcher
        MappingStore store = m.match(src, dst);


        for (Tree t : dst.breadthFirst()) {

            var srcTree = store.getSrcForDst(t);
            if (store.getSrcForDst(t) == null) {
                //that's new stuff in dst that wasn't in sources, apply the new commiId
                GumTreeFacade.appendMetadata(t, BLAME_ID, commitId, false);

            } else {
                GumTreeFacade.appendMetadata(t, BLAME_ID, srcTree.getMetadata(BLAME_ID), false);
            }
            //GumTreeFacade.showTree(dst, "[final]", System.out);


        }
        //GumTreeFacade.showTree(dst, "[final]", System.out);
    }

    public List<ReqMatch> getReqMatcher(List<Diff> diffs, boolean doMethods, boolean doInstructions) {

        Tree currentTree = null;
        for (Diff diff : diffs) {
            try {
                if (diff.src == null) {
                    GumTreeFacade.appendMetadata(diff.dst, GumTreeFacade.BLAME_ID,
                            diff.commitId, true);
                } else {
                    this.labelDestWithCommit(diff.src, diff.dst, diff.commitId);
                }
                //showTree(diff.dst, "", System.out);
                currentTree = diff.dst;
            } catch (UnsupportedOperationException e) {

                e.printStackTrace();
                System.exit(-2);
            }

            //GumTreeFacade.showTree(currentTree, "", System.out);
        }

        return GumTreeFacade.getReqMatcher(currentTree, doMethods, doInstructions);

    }

}
