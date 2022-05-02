package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeVisitor;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Collection;

public abstract class JavaParserTreeVisitorComposite extends JavaParserTreeVisitor implements TreeVisitor {

    public JavaParserTreeVisitorComposite(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);

    }

    @Override
    public void startTree(Tree tree) {
        try {
            for (Tree subtree : tree.getChildren()) {
                String subtreeType = subtree.toTreeString();
                for (Class<? extends JavaParserTreeVisitor> subVisitorClass : this.getChildVisitors()) {

                    if (canSubvisitorHandleTree(subtreeType, subVisitorClass)) {
                        JavaParserTreeVisitor subVisitor = subVisitorClass
                                .getDeclaredConstructor(TreeContext.class, ReqMatcherBuilder.class)
                                .newInstance(ctx, parentMatcherBuilder.getCopy());

                        subVisitor.startTree(subtree);
                        subVisitor.endTree(subtree);
                        matchers.addAll(subVisitor.getMatchers());
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
