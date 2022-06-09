package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

public class JavaParserTreeVisitorHelper extends JavaParserTreeVisitor {

    public JavaParserTreeVisitorHelper(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public boolean doesSupport(Type type) {
        return false;
    }

    @Override
    public void startTree(Tree tree) {
        // TODO Auto-generated method stub

    }

    @Override
    public void endTree(Tree tree) {
        // TODO Auto-generated method stub

    }

}
