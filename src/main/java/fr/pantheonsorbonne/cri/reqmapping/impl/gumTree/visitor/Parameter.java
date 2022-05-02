package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

public class Parameter extends JavaParserTreeVisitor {

    public Parameter(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);

    }

    @Override
    public void startTree(Tree tree) {


        Tree paramChild = tree.getChild(0);
        String paramType = paramChild.getType().name;
        String postpand = "";
        if (paramType.startsWith("ArrayType")) {
            postpand = "[]";
            paramChild = paramChild.getChild(0);
            paramType = paramChild.getType().name;
        }

        if (paramType.startsWith("ClassOrInterfaceType")) {
            this.parentMatcherBuilder.arg(paramChild.getChild(0).getLabel() + postpand);
        } else if (paramType.startsWith("PrimitiveType")) {
            this.parentMatcherBuilder.arg(paramChild.getLabel() + postpand);

        }


    }

    @Override
    public void endTree(Tree tree) {
        // TODO Auto-generated method stub

    }

}
