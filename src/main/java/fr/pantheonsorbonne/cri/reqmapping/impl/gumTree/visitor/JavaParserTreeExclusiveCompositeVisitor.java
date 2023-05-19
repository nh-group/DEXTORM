package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Collection;
import java.util.HashSet;

public abstract class JavaParserTreeExclusiveCompositeVisitor extends JavaParserTreeVisitor implements TreeVisitor {

    public JavaParserTreeExclusiveCompositeVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public void startTree(Tree tree) {
        Collection<Tree> remainingChildren = new HashSet<>(tree.getChildren());
        try {
            //for the level-1 subnotes
            for (Tree subtree : tree.getChildren()) {
                if (!remainingChildren.contains(subtree)) {
                    //already delt with
                    continue;
                }
                //for each childViritor
                for (Class<? extends JavaParserTreeVisitor> subVisitorClass : this.getChildVisitors()) {
                    //create the new visitor
                    JavaParserTreeVisitor subVisitor = subVisitorClass
                            .getDeclaredConstructor(Tree.class, ReqMatcherBuilder.class, Integer.TYPE, Boolean.TYPE, Boolean.TYPE)
                            .newInstance(this.tree, parentMatcherBuilder, subtree.getLine(), this.doMethods, this.doInstructions);

                    //if the type can be handled by the subvisitor
                    if (canSubvisitorHandleTree(subtree.getType(), subVisitor)) {

                        subVisitor.startTree(subtree);
                        subVisitor.endTree(subtree);
                        matchersBuilders.addAll(subVisitor.getMatchersBuilders());
                        //so that we don't process it twice with 2 visitors
                        remainingChildren.remove(subtree);
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
