package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;


import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeVisitor;
import com.google.common.base.Strings;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleRequirementGrabberCompositeVisitor implements TreeVisitor {

    private final ReqMatcherBuilder parentMatcher;
    private final List<ReqMatcherBuilder> builders;

    public SimpleRequirementGrabberCompositeVisitor(ReqMatcherBuilder parentMatcher) {
        this.parentMatcher = parentMatcher;
        this.builders = new ArrayList<>();
    }

    @Override
    public void startTree(Tree tree) {

        builders.add(this.parentMatcher.line(tree.getLine()).commits(((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID)).stream()
                .filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList())).getCopy());

        for (Tree child : tree.getChildren()) {
            new SimpleRequirementGrabberCompositeVisitor(parentMatcher).startTree(child);
        }
    }

    @Override
    public void endTree(Tree tree) {
        // TODO Auto-generated method stub

    }

    public Collection<? extends ReqMatcherBuilder> collect() {
        return builders;
    }
}
