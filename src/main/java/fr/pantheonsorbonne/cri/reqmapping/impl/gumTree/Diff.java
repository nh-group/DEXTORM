package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.nio.file.Path;

public class Diff {

    public Path src;
    public Path dst;
    public String commitId;

    public Diff(Path src, Path dst, String commitId) {
        this.src = src;
        this.dst = dst;
        this.commitId = commitId;
    }

    public DiffTree toDiffTree() throws UnsupportedOperationException, IOException {
        Tree src = null;
        if (this.src != null) {
            src = TreeGenerators.getInstance().getTree(this.src.toString()).getRoot();
        }
        var dst = TreeGenerators.getInstance().getTree(this.dst.toString()).getRoot();

        return new DiffTree(src, dst, this.commitId);
    }
}