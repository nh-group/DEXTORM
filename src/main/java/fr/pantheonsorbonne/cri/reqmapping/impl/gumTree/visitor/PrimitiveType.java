package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

public class PrimitiveType extends JavaParserTreeVisitorHelper {

    public PrimitiveType(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);

    }

    @Override
    public void startTree(ITree tree) {
        this.parentMatcherBuilder.arg(tree.getChild(0).getLabel());
    }


}
