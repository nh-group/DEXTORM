package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import java.util.Collection;
import java.util.Collections;

import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public abstract class Utils {

	public static String dump(TreeContext ct) {
		StringBuilder sb = new StringBuilder();
		for (ITree tr : ct.getRoot().getTrees()) {
			sb.append(tr.toPrettyString(ct)).append("//").append(tr.getMetadata(GumTreeFacade.BLAME_ID)).append("\n");
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static void updateMetadata(ITree t, String key, Object value) {

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

		if (t.getHeight() > 0) {
			for (ITree tt : t.getChildren()) {
				updateMetadata(tt, key, value);
			}
		}

	}

	public static Collection<ReqMatcher> getReqMatcher(final TreeContext ctx) {

		CompilationUnitVisitor visitor = new CompilationUnitVisitor(ctx, ReqMatcher.newBuilder());
		TreeUtils.visitTree(ctx.getRoot(),visitor  );
		
		for( ReqMatcherBuilder builder : visitor.getMatchers()) {
			System.out.println(builder.build().toString());
		}

		TreeUtils.visitTree(ctx.getRoot(), new TreeVisitor() {

			String tabs = "";

			@Override
			public void startTree(ITree tree) {
				tabs += "\t";
				System.out.println(tabs + tree.toPrettyString(ctx));

			}

			@Override
			public void endTree(ITree tree) {
				tabs = tabs.substring(0, tabs.length() - 1);

			}

		});

		return Collections.EMPTY_SET;

	}
}
