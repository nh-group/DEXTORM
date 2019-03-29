package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.RootsClassifier;
import com.github.gumtreediff.actions.TreeClassifier;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeUtils;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.hash.TIntIntHashMap;

public class GumTreeFacade {

	public GumTreeFacade() {

		Run.initGenerators();

	}

	public static final String BLAME_ID = "blameid";

	public void labelDestWithCommit(DiffTree d) throws UnsupportedOperationException, IOException {

		Matcher m = Matchers.getInstance().getMatcher(d.src.getRoot(), d.dst.getRoot()); // retrieve the default matcher
		m.match();
		MappingStore store = m.getMappings();

		for (ITree t : d.dst.getRoot().getTrees()) {
			System.out.println("@before" + t.toPrettyString(d.dst) + " " + t.getMetadata(BLAME_ID));
			if (store.getSrc(t) != null) {
				Utils.appendMetadata(t, BLAME_ID, store.getSrc(t).getMetadata(BLAME_ID), false);
			} else {

				Utils.appendMetadata(t, BLAME_ID, d.commitId, false);
			}
			System.out.println("@after" + t.toPrettyString(d.dst) + " " + t.getMetadata(BLAME_ID));

		}

//		TreeClassifier c = new RootsClassifier(d.src, d.dst, m);
//
//		for (ITree t : d.dst.getRoot().getTrees()) {
//
//			// System.out.println("before " + t.toPrettyString(d.dst) + " // " +
//
//			if (c.getDstMvTrees().contains(t)) { // MV
//
//				Utils.appendMetadata(t, BLAME_ID, d.commitId);
//
//			} else if (c.getDstUpdTrees().contains(t)) { // UPD
//
//				Utils.appendMetadata(t, BLAME_ID, d.commitId);
//
//			} else if (c.getDstAddTrees().contains(t)) { // add
//				
//				Utils.appendMetadata(t, BLAME_ID, d.commitId);
//				
//			} else {
//
//			}

		// System.out.println("after " + t.toPrettyString(d.dst) + " // " +
		// t.getMetadata(BLAME_ID));

//		}

	}

}
