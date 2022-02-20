package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher.ReqMatcherBuilder;

public class Parameter extends JavaParserTreeVisitor {

	public Parameter(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);

	}

	@Override
	public void startTree(ITree tree) {

		
		ITree paramChild = tree.getChild(0);
		String paramType = paramChild.toPrettyString(ctx);
		String postpand = "";
		if (paramType.startsWith("ArrayType")) {
			postpand = "[]";
			paramChild = paramChild.getChild(0);
			paramType = paramChild.toPrettyString(ctx);
		}

		if (paramType.startsWith("ClassOrInterfaceType")) {
			this.parentMatcher.arg(paramChild.getChild(0).getLabel() + postpand);
		} else if (paramType.startsWith("PrimitiveType")) {
			this.parentMatcher.arg(paramChild.getLabel() + postpand);

		}

		
	}

	@Override
	public void endTree(ITree tree) {
		// TODO Auto-generated method stub

	}

}
