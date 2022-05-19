package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.ArrayList;
import java.util.Collection;

public abstract class JavaParserTreeVisitor extends TreeVisitor.DefaultTreeVisitor {
    protected final ReqMatcherBuilder parentMatcherBuilder;
    protected Tree tree;
    protected Collection<ReqMatcherBuilder> matchers = new ArrayList<>();

    public JavaParserTreeVisitor(Tree tree, ReqMatcherBuilder treeBuilder) {
        this.tree = tree;
        this.parentMatcherBuilder = treeBuilder;
    }

    public Collection<ReqMatcherBuilder> getMatchers() {
        return this.matchers;
    }
}
