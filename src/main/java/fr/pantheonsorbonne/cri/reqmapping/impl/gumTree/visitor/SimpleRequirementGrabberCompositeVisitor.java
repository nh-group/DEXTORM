package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

import com.github.gumtreediff.tree.ITree;
import com.google.common.base.Strings;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleRequirementGrabberCompositeVisitor implements com.github.gumtreediff.tree.TreeUtils.TreeVisitor {

    private final ReqMatcherBuilder parentMatcher;

    public SimpleRequirementGrabberCompositeVisitor(ReqMatcherBuilder parentMatcher) {
        this.parentMatcher = parentMatcher;
    }

    @Override
    public void startTree(ITree tree) {
        this.parentMatcher.commits(((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID)).stream()
                .filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList()));

        for (ITree child : tree.getChildren()) {
            new SimpleRequirementGrabberCompositeVisitor(parentMatcher).startTree(child);
        }
    }

    @Override
    public void endTree(ITree tree) {
        // TODO Auto-generated method stub

    }

}
