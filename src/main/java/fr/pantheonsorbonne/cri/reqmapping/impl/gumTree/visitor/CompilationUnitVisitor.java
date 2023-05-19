package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.Arrays;
import java.util.Collection;

public class CompilationUnitVisitor extends JavaParserTreeExclusiveCompositeVisitor implements TreeVisitor {

    public CompilationUnitVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public boolean doesSupport(Type type) {
        return type.name.equals("CompilationUnit");
    }

    @Override
    public void startTree(Tree tree) {

        for (Tree child : tree.getChildren()) {
            if (child.getType().name.equals("PackageDeclaration")) {
                this.parentMatcherBuilder.packageName(child.getLabel());
                break;
            }
        }

        super.startTree(tree);

    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Arrays.asList(ClassOrInterfaceDeclarationVisitor.class,PackageDeclarationVisitor.class);
    }

}
