package fr.pantheonsorbonne.cri.app;

import com.github.gumtreediff.client.Run;
import fr.pantheonsorbonne.cri.reqmapping.MethodReqMatchImpl;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.Diff;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestDiffGumtree {

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
        Run.initGenerators();
        Diff.DiffBuilder builder = Diff.getBuilder();
        builder.add(f1.file, f1.commitId);
        builder.add(f2.file, f2.commitId);
        builder.add(f3.file, f3.commitId);
        builder.add(f4.file, f4.commitId);


        GumTreeFacade facade = new GumTreeFacade();

        Collection<ReqMatch> reqMatcherImpls = facade.getReqMatcher(builder.build(), true, true);

        //DiffTree dt = diffs.get(3).toDiffTree();
        //TreeUtils.visitTree(dt.dst.getRoot(), new PrettyBlameTreePrinter(dt.dst));

        assertEquals(3, reqMatcherImpls.size());
        boolean[] assertions = new boolean[]{false, false, false};

        for (ReqMatch m : reqMatcherImpls) {
            MethodReqMatchImpl mrm = (MethodReqMatchImpl) m;
            if (mrm.getFQClassName().equals("toto.A") && mrm.getMethodName().equals("main")) {
                assertEquals(1, m.getRequirementsIds().stream().distinct().count());
                List<String> commits = m.getRequirementsIds().stream().distinct().collect(Collectors.toList());
                assertTrue(commits.contains("commit1"));
                assertions[0] = true;
            } else if (mrm.getFQClassName().equals("toto.A") && mrm.getMethodName().equals("sum2")) {
                assertEquals(1, m.getRequirementsIds().stream().distinct().count());
                List<String> commits = m.getRequirementsIds().stream().distinct().collect(Collectors.toList());
                assertTrue(commits.contains("commit2"));
                assertions[1] = true;
            } else if (mrm.getFQClassName().equals("toto.A") && mrm.getMethodName().equals("toto")) {
                assertEquals(1, m.getRequirementsIds().stream().distinct().count());
                List<String> commits = m.getRequirementsIds().stream().distinct().collect(Collectors.toList());
                assertTrue(commits.contains("commit2"));
                assertions[2] = true;
            } else {
                fail();
            }

        }
        assertArrayEquals(new boolean[]{true, true, true}, assertions);

    }

}
