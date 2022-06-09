package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

public class ParameterVisitor extends JavaParserTreeVisitor {

    public ParameterVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public boolean doesSupport(Type type) {
        return type.name.equals("Parameter");
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
