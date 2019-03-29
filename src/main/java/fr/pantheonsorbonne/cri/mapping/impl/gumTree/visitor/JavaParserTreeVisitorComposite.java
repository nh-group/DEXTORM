package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFacade;

public abstract class JavaParserTreeVisitorComposite extends JavaParserTreeVisitor implements TreeVisitor {

	public JavaParserTreeVisitorComposite(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);

	}

	public abstract Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors();

	protected boolean canSubvisitorHandleTree(String subtreeType,
			Class<? extends JavaParserTreeVisitor> subVisitorClass) {
		return subtreeType.startsWith(subVisitorClass.getSimpleName());
	}

	@Override
	public void startTree(ITree tree) {
		try {
			for (ITree subtree : tree.getChildren()) {
				String subtreeType = subtree.toPrettyString(ctx);
				for (Class<? extends JavaParserTreeVisitor> subVisitorClass : this.getChildVisitors()) {

					if (canSubvisitorHandleTree(subtreeType, subVisitorClass)) {
						JavaParserTreeVisitor subVisitor = subVisitorClass
								.getDeclaredConstructor(TreeContext.class, ReqMatcherBuilder.class)
								.newInstance(ctx, parentMatcher.clone());

						Collection<String> commitIds = (Collection<String>) subtree.getMetadata(GumTreeFacade.BLAME_ID);
						subVisitor.parentMatcher.commits(commitIds);
						subVisitor.startTree(subtree);
						subVisitor.endTree(subtree);
						matchers.addAll(subVisitor.getMatchers());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	@Override
	public void endTree(ITree tree) {

	}

}
