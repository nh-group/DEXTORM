package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.Type;
import com.github.javaparser.ast.stmt.Statement;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.Collection;
import java.util.Collections;

public class StatementVisitor extends JavaParserTreeCompositeVisitor {


    public StatementVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    private static boolean isAStatement(Tree tree) {
        try {
            return Statement.class.isAssignableFrom(Class.forName(("com.github.javaparser.ast.stmt." + tree.getType().toString())));
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    @Override
    public void startTree(Tree tree) {
        /*
        builders.add(
                this.parentMatcher
                        .methodName(null)
                        .args(Collections.emptyList())
                        .line(tree.getLine() + this.parentLineNumber)
                        .commits(((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID)).stream().filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList()))
                        .getCopy());
        */

        tree.getChildren().stream().filter(MethodDeclarationVisitor::isAStatement).forEach(t -> {
            this.getMatchersBuilders().add(this.parentMatcherBuilder.getCopy().commits((Collection<String>) (t.getMetadata(GumTreeFacade.BLAME_ID))).line(t.getLine()));
            StatementVisitor statementVisitor = new StatementVisitor(t, this.parentMatcherBuilder, t.getLine(), this.doMethods, this.doInstructions);
            statementVisitor.startTree(t);
            this.getMatchersBuilders().addAll(statementVisitor.collect());

        });


    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void endTree(Tree tree) {
        // TODO Auto-generated method stub

    }

    public Collection<? extends ReqMatcherBuilder> collect() {
        return this.getMatchersBuilders();
    }

    @Override
    public boolean doesSupport(Type type) {
        return false;
    }
}
