package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils.TreeVisitor;

public final class PrettyBlameTreePrinter implements TreeVisitor {
	private final TreeContext ctx;
	String tabs = "";

	public PrettyBlameTreePrinter(TreeContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void startTree(ITree tree) {
		tabs += "\t";
		System.out.println(tabs + tree.toPrettyString(ctx) + tree.getMetadata(GumTreeFacade.BLAME_ID));

	}

	@Override
	public void endTree(ITree tree) {
		tabs = tabs.substring(0, tabs.length() - 1);

	}
}