package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.tree.TreeContext;

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

        TreeContext srcTreeContext = null;
        if (this.src != null) {
            srcTreeContext = Generators.getInstance().getTree(this.src.toString());
        }
        TreeContext dstTreeContext = Generators.getInstance().getTree(this.dst.toString());
        return new DiffTree(srcTreeContext, dstTreeContext, this.commitId);
    }
}