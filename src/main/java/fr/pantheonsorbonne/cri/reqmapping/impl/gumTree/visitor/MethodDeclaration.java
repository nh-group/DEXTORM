package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.Utils;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MethodDeclaration extends JavaParserTreeVisitor {

    public MethodDeclaration(TreeContext ctx, ReqMatcherBuilder treeBuilder) {
        super(ctx, treeBuilder);

    }

    private void showTree(ITree tree, String offset) {
        System.out.println(offset + tree.toShortString() + " " + tree.getLabel() + " " + tree.toPrettyString(ctx));
        offset += "\t";
        for (ITree subtree : tree.getChildren()) {
            if (!"BlockStmt".equals(subtree.toPrettyString(ctx))) {
                showTree(subtree, offset);
            }
        }

    }

    @Override
    public void startTree(ITree tree) {

        //showTree(tree, "");

        Optional<ITree> methodName = tree.getChildren().stream()
                .filter((ITree child) -> child.toPrettyString(ctx).startsWith("SimpleName")).findFirst();
        List<List<ITree>> parameters = tree.getChildren().stream()
                .filter((ITree child) -> child.toPrettyString(ctx).equals("Parameter"))
                .map(p -> p.getDescendants()).collect(Collectors.toList());


        if (methodName.isPresent()) {
            System.out.print(methodName.get().getLabel());
            StringBuilder strParameter = new StringBuilder("(");
            for (List<ITree> parameter : parameters) {

                int analyserIndex = 0;
                if (parameter.get(analyserIndex).toPrettyString(ctx).equals("ArrayType")) {
                    strParameter.append("[");
                    analyserIndex++;
                }
                if ("ClassOrInterfaceType".equals(parameter.get(analyserIndex).toPrettyString(ctx))) {
                    strParameter.append("L");
                    strParameter.append(parameter.get(++analyserIndex).getLabel());

                } else {
                    strParameter.append(Utils.typeToCodeJVM(parameter.get(analyserIndex).getLabel()));
                }

                strParameter.append(";");
                //System.out.println("\t" + strParameter);


            }
            strParameter.append(")V");


            ReqMatcherBuilder currentMethodMatcher = this.parentMatcherBuilder
                    .methodName(methodName.get().getLabel())
                    .commits((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID))
                    .args(strParameter.toString())
                    .getCopy();

            /**
             for (ITree child : tree.getChildren()) {
             String treeType = child.toPrettyString(this.ctx);
             if (treeType.equals(Parameter.class.getSimpleName())) {
             new Parameter(ctx, currentMethodMatcher).startTree(child);
             } else if (treeType.equals("BlockStmt")) {
             new SimpleRequirementGrabberCompositeVisitor(currentMethodMatcher).startTree(child);
             }
             }*/
            this.matchers.add(currentMethodMatcher);
        }

    }

    @Override
    public void endTree(ITree tree) {

    }

}
