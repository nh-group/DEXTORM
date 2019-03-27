package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import java.util.Arrays;
import java.util.Collection;

import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor.ClassOrInterfaceDeclaration;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor.JavaParserTreeVisitor;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor.JavaParserTreeVisitorComposite;

public class CompilationUnitVisitor extends JavaParserTreeVisitorComposite implements TreeVisitor {

	public CompilationUnitVisitor(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);

	}

	@Override
	public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
		return Arrays.asList(ClassOrInterfaceDeclaration.class);
	}

}
