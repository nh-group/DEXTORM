package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.ArrayList;
import java.util.Collection;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;

public abstract class JavaParserTreeVisitor implements TreeVisitor {
	protected TreeContext ctx;
	protected ReqMatcherBuilder treeBuilder;
	protected Collection<ReqMatcherBuilder> matchers = new ArrayList<>();
	
	public JavaParserTreeVisitor(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		this.ctx = ctx;
		this.treeBuilder = treeBuilder;
	}

	public Collection<ReqMatcherBuilder> getMatchers() {
		return this.matchers;
	}
}
