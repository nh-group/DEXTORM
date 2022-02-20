package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import java.util.ArrayList;
import java.util.Collection;

import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher.ReqMatcherBuilder;

public abstract class JavaParserTreeVisitor implements TreeVisitor {
	protected TreeContext ctx;
	protected final ReqMatcherBuilder parentMatcher;
	protected Collection<ReqMatcherBuilder> matchers = new ArrayList<>();
	
	public JavaParserTreeVisitor(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
		this.ctx = ctx;
		this.parentMatcher = treeBuilder;
	}

	public Collection<ReqMatcherBuilder> getMatchers() {
		return this.matchers;
	}
}
