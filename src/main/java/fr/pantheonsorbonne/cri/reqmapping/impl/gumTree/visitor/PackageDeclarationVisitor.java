package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.Type;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PackageDeclarationVisitor extends JavaParserTreeExclusiveCompositeVisitor {
    public PackageDeclarationVisitor(Tree tree, ReqMatcherBuilder treeBuilder, int startLine, boolean doMethods, boolean doInstructions) {
        super(tree, treeBuilder, startLine, doMethods, doInstructions);
    }

    @Override
    public void startTree(Tree tree) {



        super.startTree(tree);

        Set<String> issueId = tree.getChildren().stream().map(c -> (List<String>)c.getMetadata(GumTreeFacade.BLAME_ID)).flatMap(List::stream).collect(Collectors.toSet());

        ReqMatcherBuilder packageDeclarationMatcher = this.parentMatcherBuilder.getCopy()
                .line(tree.getLine()).pos(tree.getPos()).len(tree.getLength())
                .packageName(tree.getLabel())
                .issues(issueId);

        this.matchersBuilders.add(packageDeclarationMatcher);

    }

    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean doesSupport(Type type) {
        return type.name.equals("PackageDeclaration");
    }
}
