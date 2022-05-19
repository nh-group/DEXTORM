package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

public class ClassOrInterfaceType extends JavaParserTreeVisitorHelper {

    public ClassOrInterfaceType(Tree tree, ReqMatcherBuilder treeBuilder) {
        super(tree, treeBuilder);
    }

}
