package fr.pantheonsorbonne.cri.app;

import fr.pantheonsorbonne.cri.reqmapping.MethodReqMatchImpl;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.GitBlameFileRequirementProvider;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestDiffBlame {


    @Test
    void test() throws IOException, GitAPIException {
        Path tmpDir = Files.createTempDirectory("");
        Files.createDirectory(Path.of(tmpDir.toAbsolutePath().toString(), "toto"));
        Git git = Git.init().setDirectory(tmpDir.toFile()).call();
        //System.out.println(tmpDir);
        Files.copy(Paths.get("src/test/resources/A1.java"), Path.of(tmpDir.toAbsolutePath().toString(), "toto/A.java"), StandardCopyOption.REPLACE_EXISTING);
        git.add().addFilepattern("toto/A.java").call();
        git.commit().setMessage("commit1 #1").call();
        Files.copy(Paths.get("src/test/resources/A2.java"), Path.of(tmpDir.toAbsolutePath().toString(), "toto/A.java"), StandardCopyOption.REPLACE_EXISTING);
        git.add().addFilepattern("toto/A.java").call();
        git.commit().setMessage("commit2 #2").call();
        Files.copy(Paths.get("src/test/resources/A3.java"), Path.of(tmpDir.toAbsolutePath().toString(), "toto/A.java"), StandardCopyOption.REPLACE_EXISTING);
        git.add().addFilepattern("toto/A.java").call();
        git.commit().setMessage("commit3 #3").call();
        Files.copy(Paths.get("src/test/resources/A4.java"), Path.of(tmpDir.toAbsolutePath().toString(), "toto/A.java"), StandardCopyOption.REPLACE_EXISTING);
        git.add().addFilepattern("toto/A.java").call();
        git.commit().setMessage("commit4 #4").call();

        GitBlameFileRequirementProvider blame = new GitBlameFileRequirementProvider("", git.getRepository(), false, true);
        Collection<ReqMatch> reqs = blame.getReqMatcher(Path.of(tmpDir.toString(), "toto/A.java"));

        //reqs.stream().forEach(System.out::println);

        assertEquals(3, reqs.size());
        boolean[] assertions = new boolean[]{false, false, false};

        for (ReqMatch m : reqs) {
            MethodReqMatchImpl mrm = (MethodReqMatchImpl) m;
            if (mrm.getFQClassName().equals("toto.A") && mrm.getMethodName().equals("main")) {
                assertEquals(1, m.getRequirementsIds().stream().distinct().count());
                List<String> commits = m.getRequirementsIds().stream().distinct().collect(Collectors.toList());
                assertTrue(commits.contains("1"));
                assertions[0] = true;
            } else if (mrm.getFQClassName().equals("toto.A") && mrm.getMethodName().equals("sum2")) {
                assertEquals(1, m.getRequirementsIds().stream().distinct().count());
                List<String> commits = m.getRequirementsIds().stream().distinct().collect(Collectors.toList());
                assertTrue(commits.contains("4"));
                assertions[1] = true;
            } else if (mrm.getFQClassName().equals("toto.A") && mrm.getMethodName().equals("toto")) {
                assertEquals(1, m.getRequirementsIds().stream().distinct().count());
                List<String> commits = m.getRequirementsIds().stream().distinct().collect(Collectors.toList());
                assertTrue(commits.contains("3"));
                assertions[2] = true;
            } else {
                fail();
            }

        }
        assertArrayEquals(new boolean[]{true, true, true}, assertions);
    }


}
