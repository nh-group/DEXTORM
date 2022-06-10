package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Arrays;
import java.util.Collection;


public class ClassOrInterfaceDeclarationVisitor extends JavaParserTreeExclusiveCompositeVisitor {

    public ClassOrInterfaceDeclarationVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public boolean doesSupport(Type type) {
        return type.name.equals("ClassOrInterfaceDeclaration");
    }

    @Override
    public void startTree(Tree tree) {

        String className = tree.getChildren().stream()
                .filter((Tree t) -> t.toTreeString().startsWith("SimpleName")).findFirst().orElseThrow().getLabel();


        this.parentMatcherBuilder.className(className);
        super.startTree(tree);


    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Arrays.asList(MethodDeclarationVisitor.class, ConstructorVisitor.class);
    }

}
