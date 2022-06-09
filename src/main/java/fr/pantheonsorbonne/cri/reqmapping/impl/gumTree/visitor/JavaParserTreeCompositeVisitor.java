package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Collection;

public abstract class JavaParserTreeCompositeVisitor extends JavaParserTreeVisitor implements TreeVisitor {

    public JavaParserTreeCompositeVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public void startTree(Tree tree) {
        try {
            for (Tree subtree : tree.getChildren()) {

                for (Class<? extends JavaParserTreeVisitor> subVisitorClass : this.getChildVisitors()) {

                    JavaParserTreeVisitor subVisitor = subVisitorClass
                            .getDeclaredConstructor(Tree.class, ReqMatcherBuilder.class, Integer.TYPE, Boolean.TYPE, Boolean.TYPE)
                            .newInstance(this.tree, parentMatcherBuilder.getCopy(), subtree.getLine(), this.doMethods, this.doInstructions);

                    if (canSubvisitorHandleTree(subtree.getType(), subVisitor)) {

                        subVisitor.startTree(subtree);
                        subVisitor.endTree(subtree);
                        matchersBuilders.addAll(subVisitor.getMatchersBuilders());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public abstract Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors();

    protected boolean canSubvisitorHandleTree(Type subtreeType,
                                              JavaParserTreeVisitor subVisitorClass) {
        return subVisitorClass.doesSupport(subtreeType);
    }

    @Override
    public void endTree(Tree tree) {

    }

}
