package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.Collection;
import java.util.Optional;

public class MethodDeclaration extends JavaParserTreeVisitor {

    public MethodDeclaration(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);

    }

    @Override
    public void startTree(ITree tree) {

        Optional<ITree> methodName = tree.getChildren().stream()
                .filter((ITree child) -> child.toPrettyString(ctx).startsWith("SimpleName")).findFirst();
        if (methodName.isPresent()) {
            ReqMatcherBuilder currentMethodMatcher = (ReqMatcherBuilder) this.parentMatcher
                    .methodName(methodName.get().getLabel())
                    .commits((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID))
                    .clone();

            for (ITree child : tree.getChildren()) {
                String treeType = child.toPrettyString(this.ctx);
                if (treeType.equals(Parameter.class.getSimpleName())) {
                    new Parameter(ctx, currentMethodMatcher).startTree(child);
                } else if (treeType.equals("BlockStmt")) {
                    new SimpleRequirementGrabberCompositeVisitor(currentMethodMatcher).startTree(child);
                }
            }
            this.matchers.add(currentMethodMatcher);
        }

    }

    @Override
    public void endTree(ITree tree) {

    }

}
