package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Collection;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public class PrimitiveType extends JavaParserTreeVisitorHelper {

	public PrimitiveType(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);

	}

	@Override
	public void startTree(ITree tree) {
		this.parentMatcher.arg(tree.getChild(0).getLabel());
	}

	

}
