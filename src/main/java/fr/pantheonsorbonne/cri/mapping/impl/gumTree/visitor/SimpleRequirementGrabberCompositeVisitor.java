package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.javaparser.ast.visitor.TreeVisitor;
import com.google.common.base.Strings;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFacade;

public class SimpleRequirementGrabberCompositeVisitor implements com.github.gumtreediff.tree.TreeUtils.TreeVisitor {

	public SimpleRequirementGrabberCompositeVisitor(ReqMatcherBuilder parentMatcher) {
		this.parentMatcher = parentMatcher;
	}

	private final ReqMatcherBuilder parentMatcher;

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
