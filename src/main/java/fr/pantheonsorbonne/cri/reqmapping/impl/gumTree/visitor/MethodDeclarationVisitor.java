package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.Type;
import com.github.javaparser.ast.stmt.Statement;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.Utils;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MethodDeclarationVisitor extends JavaParserTreeExclusiveCompositeVisitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodDeclarationVisitor.class);

    public MethodDeclarationVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    protected static boolean isAStatement(Tree tree) {
        try {
            return Statement.class.isAssignableFrom(Class.forName(("com.github.javaparser.ast.stmt." + tree.getType().toString())));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean doesSupport(Type type) {
        return type.name.equals("MethodDeclaration");
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

        String methodName = tree.getChildren().stream()
                .filter((Tree child) -> child.getType().name.startsWith("SimpleName")).findFirst().orElseThrow().getLabel();
        List<List<Tree>> parameters = tree.getChildren().stream()
                .filter((Tree child) -> child.getType().name.equals("Parameter"))
                .map(p -> p.getDescendants()).collect(Collectors.toList());


        String strParameter = extractMethodLikeParameters(parameters);
        //String trace = this.parentMatcherBuilder.getPackageName() + "." + this.parentMatcherBuilder.getClassName() + "." + methodName.get().getLabel() + "(" + strParameter + ")" + ((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID)).stream().collect(Collectors.joining("-"));
        //System.out.println("#" + trace);

        if (this.doInstructions) {
            extractStatement(tree);
        }

        if (this.doMethods) {
            extractMethodLike(tree, methodName, strParameter);
        }


    }

    private String extractMethodLikeParameters(List<List<Tree>> parameters) {
        StringBuilder strParameter = new StringBuilder("(");
        for (List<Tree> parameter : parameters) {

            int analyserIndex = 0;
            if (parameter.get(analyserIndex).getType().name.equals("SingleMemberAnnotationExpr")) {
                analyserIndex += 3;
            }
            while (parameter.get(analyserIndex).getType().name.equals("ArrayType")) {
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
        return strParameter.toString();
    }

    protected void extractStatement(Tree tree) {
        for (Tree child : tree.getChildren()) {
            //find the code block for this method
            if (child.getType().name.equals("BlockStmt")) {
                StatementVisitor statementVisitor = new StatementVisitor(child, this.parentMatcherBuilder.getCopy(), this.startLine, this.doMethods, this.doInstructions);
                statementVisitor.startTree(child);
                this.matchersBuilders.addAll(statementVisitor.collect());
                break;
            }
        }
    }

    protected void extractMethodLike(Tree tree, String methodName, String strParameter) {
        ReqMatcherBuilder currentMethodMatcher = this.parentMatcherBuilder
                .methodName(methodName)
                .args(strParameter)
                .commits((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID))
                .getCopy();
        this.matchersBuilders.add(currentMethodMatcher);
    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void endTree(Tree tree) {

    }

}
