import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.gumtreediff.tree.TreeContext;
import com.google.common.collect.Lists;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.Diff;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.DiffTree;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFacade;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.Utils;

class TestDiffBlame {

	private static Path meterialize(String fileContent) throws IOException {
		Path f = Files.createTempFile("", ".java");
		FileWriter fw = new FileWriter(f.toFile());
		fw.write(fileContent.toCharArray());
		fw.close();
		return f;

	}

	class CommitFileMaterialization {

		public CommitFileMaterialization(Path file, String commitId) {
			this.file = file;
			this.commitId = commitId;
		}

		public Path file;
		public String commitId;
	}

	@Test
	void test() throws IOException {
		CommitFileMaterialization f1 = new CommitFileMaterialization(Paths.get("src/test/resources/A1.java"),
				"commit1");
		CommitFileMaterialization f2 = new CommitFileMaterialization(Paths.get("src/test/resources/A2.java"),
				"commit2");
		CommitFileMaterialization f3 = new CommitFileMaterialization(Paths.get("src/test/resources/A3.java"),
				"commit3");

		List<CommitFileMaterialization> files = Lists.newArrayList(f1, f2, f3);

		GumTreeFacade facade = new GumTreeFacade();

		Diff diff = new Diff(null, null, null);
		TreeContext currentContext = null;
		for (CommitFileMaterialization file : files) {
			diff.dst = file.file;
			DiffTree current = diff.toDiffTree();
			current.commitId = file.commitId;

			if (currentContext == null) {
				Utils.updateMetadata(current.dst.getRoot(), GumTreeFacade.BLAME_ID, file.commitId);
			} else {
				current.src = currentContext;
				facade.labelDestWithCommit(current);
			}
			currentContext = current.dst;

		}

		System.out.println(Utils.dump(currentContext));

		Collection<ReqMatcher> reqMatcher = Utils.getReqMatcher(currentContext);

	}

}
