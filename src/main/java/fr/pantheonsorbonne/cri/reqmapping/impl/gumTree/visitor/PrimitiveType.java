package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

public class PrimitiveType extends JavaParserTreeVisitorHelper {

    public PrimitiveType(Tree tree, ReqMatcherBuilder treeBuilder) {
        super(tree, treeBuilder);

    }

    @Override
    public void startTree(Tree tree) {
        this.parentMatcherBuilder.arg(tree.getChild(0).getLabel());
    }


}
