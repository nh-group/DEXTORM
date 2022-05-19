package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Arrays;
import java.util.Collection;

public class CompilationUnitVisitor extends JavaParserTreeVisitorComposite implements TreeVisitor {

    public CompilationUnitVisitor(Tree tree, ReqMatcherBuilder treeBuilder) {
        super(tree, treeBuilder);

    }

    @Override
    public void startTree(Tree tree) {

        for (Tree child : tree.getChildren()) {
            if (child.getType().name.equals("PackageDeclaration")) {
                this.parentMatcherBuilder.packageName(child.getLabel());

            }
        }

        super.startTree(tree);

    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Arrays.asList(ClassOrInterfaceDeclaration.class);
    }

}
