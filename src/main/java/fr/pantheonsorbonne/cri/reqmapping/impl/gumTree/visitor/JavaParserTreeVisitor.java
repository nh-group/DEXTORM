package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class JavaParserTreeVisitor extends TreeVisitor.DefaultTreeVisitor {
    protected final ReqMatcherBuilder parentMatcherBuilder;
    protected final int startLine;
    protected final Boolean doMethods;
    protected final Boolean doInstructions;
    protected Tree tree;
    protected List<ReqMatcherBuilder> matchersBuilders = new ArrayList<>();


    public JavaParserTreeVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        this.tree = tree;
        this.parentMatcherBuilder = treeBuilder;
        this.startLine = startLine;
        this.doInstructions = doInstructions;
        this.doMethods = doMethods;
    }

    public List<ReqMatcherBuilder> getMatchersBuilders() {
        return this.matchersBuilders;
    }

    public abstract boolean doesSupport(Type type);
}
