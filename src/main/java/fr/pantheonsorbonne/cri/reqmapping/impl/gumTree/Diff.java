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

    public Tree src;
    public Tree dst;
    public String commitId;

    private Diff(Tree src, Tree dst, String commitId) {
        this.src = src;
        this.dst = dst;
        this.commitId = commitId;

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

        public DiffBuilder add(Path file, String commitId) {
            atoms.add(new DiffAtom(file, commitId));
            return this;
        }

        public List<Diff> build() {
            List<Diff> res = new LinkedList<>();
            try {

                DiffAtom lastAtom = new DiffAtom(null, null);
                for (DiffAtom atom : this.atoms) {
                    var treeDst = TreeGenerators.getInstance().getTree(atom.src.toString()).getRoot();
                    Tree treeSrc = res.size() > 0 ? res.get(res.size() - 1).dst : null;
                    res.add(new Diff(treeSrc, treeDst, atom.commit));
                }
            } catch (IOException exp) {
                throw new RuntimeException(exp);
            }
            return res;
        }

    }

    static class DiffAtom {
        public Path src;
        public String commit;

        public DiffAtom(Path src, String commit) {
            this.src = src;
            this.commit = commit;
        }
    }


}