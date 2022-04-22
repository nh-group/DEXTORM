package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.tree.TreeContext;

public class DiffTree {

    public TreeContext src;
    public TreeContext dst;
    public String commitId;

    public DiffTree(TreeContext src, TreeContext dst, String commitId) {
        this.src = src;
        this.dst = dst;
        this.commitId = commitId;
    }
}