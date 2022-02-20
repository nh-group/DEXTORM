package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import java.io.IOException;
import java.nio.file.Path;

import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.tree.TreeContext;

public class Diff {

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

	public Path src;
	public Path dst;
	public String commitId;
}