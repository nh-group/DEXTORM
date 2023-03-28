package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.github.gumtreediff.gen.TreeGenerators;
import com.github.gumtreediff.tree.Tree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Diff {

    public final Tree src;
    public final Tree dst;
    public final String issueId;
    public final String commitId;

    private Diff(Tree src, Tree dst, String issueId,String commitId) {
        this.src = src;
        this.dst = dst;
        this.issueId = issueId;
        this.commitId=commitId;

    }


    public static DiffBuilder getBuilder() {
        return new DiffBuilder();
    }

    public static class DiffBuilder {

        private final List<DiffAtom> atoms = new ArrayList<>();

        protected DiffBuilder() {
        }

        public void dispose() {
            for (DiffAtom atom : this.atoms) {
                try {
                    Files.deleteIfExists(atom.src);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        public DiffBuilder add(Path file, String issueId,String commitId) {
            atoms.add(new DiffAtom(file, issueId,commitId));
            return this;
        }

        public List<Diff> build() {
            List<Diff> res = new LinkedList<>();
            try {

                for (DiffAtom atom : this.atoms) {
                    var treeDst = TreeGenerators.getInstance().getTree(atom.src.toString()).getRoot();
                    Tree treeSrc = res.size() > 0 ? res.get(res.size() - 1).dst : null;
                    res.add(new Diff(treeSrc, treeDst, atom.issueId,atom.commitId));
                }
            } catch (IOException exp) {
                throw new RuntimeException(exp);
            }
            return res;
        }

    }

    static class DiffAtom {
        public final Path src;
        public final String issueId;
        public final String commitId;

        public DiffAtom(Path src, String issueId,String commitId) {
            this.src = src;
            this.issueId = issueId;
            this.commitId=commitId;
        }
    }


}