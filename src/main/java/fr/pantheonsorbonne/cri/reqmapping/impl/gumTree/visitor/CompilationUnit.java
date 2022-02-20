package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import java.util.Arrays;
import java.util.Collection;


import com.github.gumtreediff.tree.TreeContext;


import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher.ReqMatcherBuilder;

public class CompilationUnit extends JavaParserTreeVisitorComposite {

	public CompilationUnit(TreeContext ctx, ReqMatcherBuilder parentMatcher) {
		super(ctx, parentMatcher);
		

	}
	
	
	
	
	@Override
	public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
		return Arrays.asList(ClassOrInterfaceDeclaration.class);
	}

}