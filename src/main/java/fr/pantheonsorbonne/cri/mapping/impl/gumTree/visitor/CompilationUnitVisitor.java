package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Arrays;
import java.util.Collection;

import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public class CompilationUnitVisitor extends JavaParserTreeVisitorComposite implements TreeVisitor {

	public CompilationUnitVisitor(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);

	}

	@Override
	public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
		return Arrays.asList(ClassOrInterfaceDeclaration.class);
	}

}
