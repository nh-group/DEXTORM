package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class JavaParserTreeVisitor extends TreeVisitor.DefaultTreeVisitor {
    protected final ReqMatcherBuilder parentMatcherBuilder;
    protected Tree tree;
    protected List<ReqMatcherBuilder> matchersBuilders = new ArrayList<>();

    public JavaParserTreeVisitor(Tree tree, ReqMatcherBuilder treeBuilder) {
        this.tree = tree;
        this.parentMatcherBuilder = treeBuilder;
    }

    public List<ReqMatcherBuilder> getMatchersBuilders() {
        return this.matchersBuilders;
    }
}
