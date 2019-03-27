package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

public class DiffTree {

	public DiffTree(TreeContext src, TreeContext dst, String commitId) {
		this.src = src;
		this.dst = dst;
		this.commitId = commitId;
	}

	public TreeContext src;
	public TreeContext dst;
	public String commitId;
}