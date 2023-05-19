package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.Collection;

public class ConstructorVisitor extends MethodDeclarationVisitor {

    private static final String INIT_METHOD_NAME = "<init>";

    public ConstructorVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public boolean doesSupport(Type type) {
        return type.name.equals("ConstructorDeclaration");
    }

    @Override
    protected void extractMethodLike(Tree tree, String methodName, String strParameter) {

        ReqMatcherBuilder currentMethodMatcher = this.parentMatcherBuilder
                .methodName(INIT_METHOD_NAME)
                .args(strParameter)
                .issues((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID))
                .getCopy();
        this.matchersBuilders.add(currentMethodMatcher);
        
    }
    //if not a constructor, do nothin
}
