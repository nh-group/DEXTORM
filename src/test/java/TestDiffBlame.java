import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.Diff;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.DiffTree;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.PrettyBlameTreePrinter;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFacade;

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
		CommitFileMaterialization f4 = new CommitFileMaterialization(Paths.get("src/test/resources/A4.java"),
				"commit4");

		List<Diff> diffs = new ArrayList<>();
		diffs.add(new Diff(null, f1.file, f1.commitId));
		diffs.add(new Diff(f1.file, f2.file, f2.commitId));
		diffs.add(new Diff(f2.file, f3.file, f3.commitId));
		diffs.add(new Diff(f3.file, f4.file, f4.commitId));

		GumTreeFacade facade = new GumTreeFacade();

		
		Collection<ReqMatcher> reqMatchers = facade.getReqMatcher(diffs);

		assertEquals(3, reqMatchers.size());
		for (ReqMatcher m : reqMatchers) {
			if (m.getClassName().equals("A") && m.getMethodName().equals("main")) {
				assertEquals(1, m.getReq().size());
				assertTrue(m.getReq().get(0).equals("commit1"));
			}
			if (m.getClassName().equals("A") && m.getMethodName().equals("sum2")) {
				assertEquals(3, m.getReq().stream().distinct().count());
				List<String> commits = m.getReq().stream().distinct().collect(Collectors.toList());
				assertTrue(m.getReq().contains("commit4"));
				assertTrue(m.getReq().contains("commit3"));
				assertTrue(m.getReq().contains("commit2"));
			}
			if (m.getClassName().equals("A") && m.getMethodName().equals("toto")) {
				assertEquals(1, m.getReq().stream().distinct().count());
				List<String> commits = m.getReq().stream().distinct().collect(Collectors.toList());
				assertTrue(m.getReq().contains("commit2"));
			}
		}
//		ReqMatcher first = new ReqMatcher("A","main", "String[]", 0, Arrays.asList(commit1))
//		A:main (String[] )//commit1:1
//		A:sum2 ( void  )//commit4:2 ,commit3:2 ,commit2:5
//		A:toto ( void  )//commit2:3	

	}

}
