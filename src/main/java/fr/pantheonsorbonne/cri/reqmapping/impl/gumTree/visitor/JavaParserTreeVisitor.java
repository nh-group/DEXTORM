package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.ArrayList;
import java.util.Collection;

public abstract class JavaParserTreeVisitor implements TreeVisitor {
    protected final ReqMatcherBuilder parentMatcherBuilder;
    protected TreeContext ctx;
    protected Collection<ReqMatcherBuilder> matchers = new ArrayList<>();

    public JavaParserTreeVisitor(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        this.ctx = ctx;
        this.parentMatcherBuilder = treeBuilder;
    }

    public Collection<ReqMatcherBuilder> getMatchers() {
        return this.matchers;
    }
}
