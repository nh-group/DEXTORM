package fr.pantheonsorbonne.cri.mapping.impl.gumTree.visitor;

import java.util.Arrays;
import java.util.Collection;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.javaparser.ast.visitor.TreeVisitor;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFacade;

public class SimpleRequirementGrabberCompositeVisitor implements com.github.gumtreediff.tree.TreeUtils.TreeVisitor {

	public SimpleRequirementGrabberCompositeVisitor(ReqMatcherBuilder parentMatcher) {
		this.parentMatcher = parentMatcher;
	}

	private final ReqMatcherBuilder parentMatcher;

	@Override
	public void startTree(ITree tree) {
		this.parentMatcher.commits((Collection<String>) tree.getMetadata(GumTreeFacade.BLAME_ID));
		for (ITree child : tree.getChildren()) {
			new SimpleRequirementGrabberCompositeVisitor(parentMatcher).startTree(child);
		}
	}

	@Override
	public void endTree(ITree tree) {
		// TODO Auto-generated method stub
		
	}

}
