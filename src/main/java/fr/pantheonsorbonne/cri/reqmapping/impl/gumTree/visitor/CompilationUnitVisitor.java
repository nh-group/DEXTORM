package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeVisitor;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Arrays;
import java.util.Collection;

public class CompilationUnitVisitor extends JavaParserTreeVisitorComposite implements TreeVisitor {

    public CompilationUnitVisitor(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);

    }

    @Override
    public void startTree(Tree tree) {

        for (Tree child : tree.getChildren()) {
            if (child.toTreeString().startsWith("PackageDeclaration")) {
                this.parentMatcherBuilder.packageName(child.getChildren().get(0).getLabel());

            }
        }

        super.startTree(tree);

    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Arrays.asList(ClassOrInterfaceDeclaration.class);
    }

}
