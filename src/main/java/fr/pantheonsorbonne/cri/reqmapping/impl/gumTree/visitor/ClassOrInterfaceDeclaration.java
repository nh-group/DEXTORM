package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeContext;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;


public class ClassOrInterfaceDeclaration extends JavaParserTreeVisitorComposite {

    public ClassOrInterfaceDeclaration(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);

    }

    @Override
    public void startTree(Tree tree) {

        Optional<Tree> classNameLeaf = tree.getChildren().stream()
                .filter((Tree t) -> t.toTreeString().startsWith("SimpleName")).findFirst();

        if (classNameLeaf.isPresent()) {
            this.parentMatcherBuilder.className(classNameLeaf.get().getLabel());
            super.startTree(tree);
        }

    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Arrays.asList(MethodDeclaration.class);
    }

}
