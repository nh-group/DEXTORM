package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Arrays;
import java.util.Collection;

import com.github.gumtreediff.gen.javaparser.JavaParserGenerator;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;
import com.github.javaparser.ast.PackageDeclaration;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public class CompilationUnitVisitor extends JavaParserTreeVisitorComposite implements TreeVisitor {

	public CompilationUnitVisitor(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		super(ctx, treeBuilder);
		System.out.println(ctx.getRoot().toPrettyString(ctx));
		System.out.println(ctx.getRoot().getLabel());
		System.out.println(ctx.getRoot().isRoot());

	}

	@Override
	public void startTree(ITree tree) {
		for (ITree child : ctx.getRoot().getChildren()) {
			if (child.toPrettyString(ctx).startsWith(PackageDeclaration.class.getSimpleName())) {
				this.parentMatcher.packageName(child.getLabel());
			}
		}
		super.startTree(tree);
	}

	@Override
	public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
		return Arrays.asList(ClassOrInterfaceDeclaration.class);
	}

}
