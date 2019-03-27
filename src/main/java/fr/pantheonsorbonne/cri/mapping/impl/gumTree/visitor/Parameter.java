package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public class Parameter extends JavaParserTreeVisitor {

	public Parameter(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);

	}

	@Override
	public void startTree(ITree tree) {
		ITree paramChild = tree.getChild(0);
		String paramType = paramChild.toPrettyString(ctx);
		String postpand = "";
		if (paramType.equals("ArrayType")) {
			postpand = "[]";
			paramChild = paramChild.getChild(0);
			paramType = paramChild.toPrettyString(ctx);
		}

		if (paramType.equals("ClassOrInterfaceType")) {
			treeBuilder.arg(paramChild.getChild(0).getLabel() + "postpand");
		} else if (paramType.equals("PrimitiveType")) {
			System.out.println(paramChild.getId());
			if (paramChild.getId() == 38) {
				treeBuilder.arg("int" + "postpand");
			} else if (paramChild.getId() == 18) {
				treeBuilder.arg("boolean" + "postpand");
			}
		}

	}

	@Override
	public void endTree(ITree tree) {
		// TODO Auto-generated method stub

	}

}
