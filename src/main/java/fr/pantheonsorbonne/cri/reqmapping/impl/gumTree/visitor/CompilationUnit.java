package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.Tree;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;

import java.util.Arrays;
import java.util.Collection;

public class CompilationUnit extends JavaParserTreeVisitorComposite {

    public CompilationUnit(Tree tree, ReqMatcherBuilder parentMatcher) {
        super(tree, parentMatcher);


    }


    @Override
    public Collection<Class<? extends JavaParserTreeVisitor>> getChildVisitors() {
        return Arrays.asList(ClassOrInterfaceDeclaration.class);
    }

}
