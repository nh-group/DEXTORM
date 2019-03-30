package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor.CompilationUnitVisitor;

public class GumTreeFacade {

	private List<Diff> diffs;

	{
		Arrays.asList(JavaParserGenerator.class).forEach(gen -> {
			com.github.gumtreediff.gen.Register a = gen.getAnnotation(com.github.gumtreediff.gen.Register.class);
			if (a != null)
				Generators.getInstance().install(gen, a);
		});
	}

	public static final String BLAME_ID = "blameid";

	public void labelDestWithCommit(DiffTree d) {

		Matcher m = Matchers.getInstance().getMatcher(d.src.getRoot(), d.dst.getRoot()); // retrieve the default matcher
		m.match();
		MappingStore store = m.getMappings();

		for (ITree t : d.dst.getRoot().getTrees()) {

			if (store.getSrc(t) != null) {
				GumTreeFacade.appendMetadata(t, BLAME_ID, store.getSrc(t).getMetadata(BLAME_ID), false);
			} else {
				GumTreeFacade.appendMetadata(t, BLAME_ID, d.commitId, false);
			}

		}
	}

	public static String dump(TreeContext ct) {
		StringBuilder sb = new StringBuilder();
		for (ITree tr : ct.getRoot().getTrees()) {
			sb.append(tr.toPrettyString(ct)).append("//").append(tr.getMetadata(BLAME_ID)).append("\n");
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static void appendMetadata(ITree t, String key, Object value, boolean recursive) {

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

		if (recursive && t.getHeight() > 0) {
			for (ITree tt : t.getChildren()) {
				appendMetadata(tt, key, value, recursive);
			}
		}

	}

	public static void addCommitMetadataToTreeRecursive(ITree t, String commitID) {
		GumTreeFacade.appendMetadata(t, BLAME_ID, commitID, true);
	}

	public static void addCommitMetadataToTree(ITree t, String commitID) {
		GumTreeFacade.appendMetadata(t, BLAME_ID, commitID, false);
	}

	private static Collection<ReqMatcher> getReqMatcher(final TreeContext ctx) {

		CompilationUnitVisitor visitor = new CompilationUnitVisitor(ctx, ReqMatcher.newBuilder());
		TreeUtils.visitTree(ctx.getRoot(), visitor);
		return visitor.getMatchers().stream().map(ReqMatcherBuilder::build).collect(Collectors.toList());

	}

	public Collection<ReqMatcher> getReqMatcher(List<Diff> diffs) {

		TreeContext currentContext = null;

		for (Diff diff : diffs) {

			DiffTree currentDiff;
			try {
				currentDiff = diff.toDiffTree();

				if (currentContext == null) {
					GumTreeFacade.appendMetadata(currentDiff.dst.getRoot(), GumTreeFacade.BLAME_ID,
							currentDiff.commitId, true);
				} else {
					currentDiff.src = currentContext;
					this.labelDestWithCommit(currentDiff);
				}
				currentContext = currentDiff.dst;
			} catch (UnsupportedOperationException | IOException e) {

				e.printStackTrace();
				System.exit(-2);
			}

		}

		return GumTreeFacade.getReqMatcher(currentContext);

	}

}
