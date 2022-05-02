package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.tree.Tree;

public class DiffTree {

    public Tree src;
    public Tree dst;
    public String commitId;

    public DiffTree(Tree src, Tree dst, String commitId) {
        this.src = src;
        this.dst = dst;
        this.commitId = commitId;
    }
}