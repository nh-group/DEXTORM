package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.TreeContext;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher.ReqMatcherBuilder;

public class ClassOrInterfaceType extends JavaParserTreeVisitorHelper {

    public ClassOrInterfaceType(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);
    }

}
