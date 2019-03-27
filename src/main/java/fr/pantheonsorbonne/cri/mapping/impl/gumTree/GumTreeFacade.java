package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.actions.RootsClassifier;
import com.github.gumtreediff.actions.TreeClassifier;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

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
		for (Mapping mapping : m.getMappingsAsSet()) {
			
			Utils.updateMetadata(mapping.second, BLAME_ID, mapping.first.getMetadata(BLAME_ID));
			
		}

		TreeClassifier c = new RootsClassifier(d.src, d.dst, m);

		for (ITree t : d.dst.getRoot().getTrees()) {
			if (c.getDstMvTrees().contains(t)) { // MV
				
				Utils.updateMetadata(t, BLAME_ID,  d.commitId);
			} else if (c.getDstUpdTrees().contains(t)) { // UPD
				
				Utils.updateMetadata(t, BLAME_ID,  d.commitId);
			} else if (c.getDstAddTrees().contains(t)) { // add
				
				Utils.updateMetadata(t, BLAME_ID,  d.commitId);
			} else {
				// don't tag it
			}

		}

	}

	public Collection<ReqMatcher> getReqMatcher() {
		// TODO Auto-generated method stub
		return null;
	}

}
