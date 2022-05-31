package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Collection;

public abstract class JavaParserTreeVisitorComposite extends JavaParserTreeVisitor implements TreeVisitor {

    public JavaParserTreeVisitorComposite(Tree tree, ReqMatcherBuilder treeBuilder) {
        super(tree, treeBuilder);

    }

    @Override
    public void startTree(Tree tree) {
        try {
            for (Tree subtree : tree.getChildren()) {
                String subtreeType = subtree.toTreeString();
                for (Class<? extends JavaParserTreeVisitor> subVisitorClass : this.getChildVisitors()) {

                    if (canSubvisitorHandleTree(subtreeType, subVisitorClass)) {
                        JavaParserTreeVisitor subVisitor = subVisitorClass
                                .getDeclaredConstructor(Tree.class, ReqMatcherBuilder.class)
                                .newInstance(this.tree, parentMatcherBuilder.getCopy());

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

    protected boolean canSubvisitorHandleTree(String subtreeType,
                                              Class<? extends JavaParserTreeVisitor> subVisitorClass) {
        return subtreeType.startsWith(subVisitorClass.getSimpleName());
    }

    @Override
    public void endTree(Tree tree) {

    }

}
