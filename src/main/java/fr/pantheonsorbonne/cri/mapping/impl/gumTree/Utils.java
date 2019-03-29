package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor.CompilationUnitVisitor;

public abstract class Utils {

	public static String dump(TreeContext ct) {
		StringBuilder sb = new StringBuilder();
		for (ITree tr : ct.getRoot().getTrees()) {
			sb.append(tr.toPrettyString(ct)).append("//").append(tr.getMetadata(GumTreeFacade.BLAME_ID)).append("\n");
		}

		return sb.toString();
	}

	public static void addCommitMetadataToTree(ITree t, String commitID) {
		appendMetadata(t, GumTreeFacade.BLAME_ID, commitID, false);
	}

	public static void addCommitMetadataToTreeRecursive(ITree t, String commitID) {
		appendMetadata(t, GumTreeFacade.BLAME_ID, commitID, true);
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
		} else {
			existingValue.add(value);
		}

		if (recursive && t.getHeight() > 0) {
			for (ITree tt : t.getChildren()) {
				appendMetadata(tt, key, value, recursive);
			}
		}

	}

	public static Collection<ReqMatcher> getReqMatcher(final TreeContext ctx) {

		CompilationUnitVisitor visitor = new CompilationUnitVisitor(ctx, ReqMatcher.newBuilder());
		TreeUtils.visitTree(ctx.getRoot(), visitor);
		return visitor.getMatchers().stream().map((ReqMatcherBuilder b) -> b.build()).collect(Collectors.toList());

	}
}
