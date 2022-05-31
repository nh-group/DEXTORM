package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

public class CodeBlockVisitor extends JavaParserTreeVisitor {
    public CodeBlockVisitor(Tree tree, ReqMatcherBuilder treeBuilder) {
        super(tree, treeBuilder);
    }

    @Override
    public void startTree(Tree tree) {
        GumTreeFacade.showTree(tree, "", System.out);
    }

    @Override
    public void endTree(Tree tree) {
    }
}
