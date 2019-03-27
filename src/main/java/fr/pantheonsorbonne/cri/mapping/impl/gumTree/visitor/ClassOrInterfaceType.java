package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Collection;

import com.github.gumtreediff.tree.TreeContext;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public class ClassOrInterfaceType extends JavaParserTreeVisitorHelper {

	public ClassOrInterfaceType(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);
	}

}
