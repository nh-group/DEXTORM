package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Arrays;
import java.util.Collection;

import com.github.gumtreediff.tree.TreeContext;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public class ClassOrInterfaceDeclaration extends JavaParserTreeVisitorComposite {

	public ClassOrInterfaceDeclaration(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);

	}

	@Override
	public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
		return Arrays.asList(MethodDeclaration.class);
	}

}