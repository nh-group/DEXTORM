package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.Utils;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class MethodDeclaration extends JavaParserTreeVisitorComposite {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodDeclaration.class);

    public MethodDeclaration(Tree tree, ReqMatcherBuilder treeBuilder) {
        super(tree, treeBuilder);

    }

    private void showTree(Tree tree, String offset) {
        //System.out.println(offset + tree.toTreeString() + " " + tree.getLabel() + " " + tree);
        offset += "\t";
        for (Tree subtree : tree.getChildren()) {
            if (!"BlockStmt".equals(subtree.getType())) {
                showTree(subtree, offset);
            }
        }

    }

    @Override
    public void startTree(Tree tree) {

        //GumTreeFacade.showTree(tree, "", System.out);

        Optional<Tree> methodName = tree.getChildren().stream()
                .filter((Tree child) -> child.getType().name.startsWith("SimpleName")).findFirst();
        List<List<Tree>> parameters = tree.getChildren().stream()
                .filter((Tree child) -> child.getType().name.equals("Parameter"))
                .map(p -> p.getDescendants()).collect(Collectors.toList());


        if (methodName.isPresent()) {
            StringBuilder strParameter = new StringBuilder("(");
            for (List<Tree> parameter : parameters) {

                int analyserIndex = 0;
                if (parameter.get(analyserIndex).getType().name.equals("SingleMemberAnnotationExpr")) {
                    analyserIndex += 3;
                }
                if (parameter.get(analyserIndex).getType().name.equals("ArrayType")) {
                    strParameter.append("[");
                    analyserIndex++;
                }
                if ("ClassOrInterfaceType".equals(parameter.get(analyserIndex).getType().name)) {
                    strParameter.append("L");
                    strParameter.append(parameter.get(++analyserIndex).getLabel());

                } else {
                    strParameter.append(Utils.typeToCodeJVM(parameter.get(analyserIndex).getLabel()));
                }

                strParameter.append(";");
                //System.out.println("\t" + strParameter);


            }
            strParameter.append(")V");
            String trace = this.parentMatcherBuilder.getPackageName() + "." + this.parentMatcherBuilder.getClassName() + "." + methodName.get().getLabel() + "(" + strParameter + ")" + ((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID)).stream().collect(Collectors.joining("-"));
            //System.out.println("#" + trace);

            ReqMatcherBuilder currentMethodMatcher = this.parentMatcherBuilder
                    .methodName(methodName.get().getLabel())
                    .commits((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID))
                    .args(strParameter.toString())
                    .getCopy();

            List<ReqMatcherBuilder> statementBuilder = new ArrayList<>();
            for (Tree child : tree.getChildren()) {
                if (child.getType().name.equals("BlockStmt")) {
                    //here the issue is that the line in the block statement and its children are relative to the line of the the parent an not relative to the file.
                    var visitor = new SimpleRequirementGrabberCompositeVisitor(currentMethodMatcher, tree.getLine());
                    //visitor.startTree(child);
                    statementBuilder.addAll(visitor.collect());
                }
            }
            this.matchersBuilders.add(currentMethodMatcher);
            this.matchersBuilders.addAll(statementBuilder);
        }

    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Arrays.asList(CodeBlockVisitor.class);
    }

    @Override
    public void endTree(Tree tree) {

    }

}
