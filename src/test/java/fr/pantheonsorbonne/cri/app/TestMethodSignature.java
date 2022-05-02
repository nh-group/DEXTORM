package fr.pantheonsorbonne.cri.app;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import fr.pantheonsorbonne.cri.reqmapping.MethodReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.BlameDataWrapper;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.ReqMatcherJavaVisitor;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.Diff;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMethodSignature {

    @Test
    public void testGT() {
        CommitFileMaterialization f1 = new CommitFileMaterialization(Paths.get("src/test/resources/A5.java"),
                "commit1");

        List<Diff> diffs = new ArrayList<>();
        diffs.add(new Diff(null, f1.file, f1.commitId));

        GumTreeFacade facade = new GumTreeFacade();

        Collection<ReqMatch> reqMatchers = facade.getReqMatcher(diffs);

        List<ReqMatch> reqMatchersList = new ArrayList<>(reqMatchers);

        methodSignatureExtraction(reqMatchersList);

    }

    private void methodSignatureExtraction(List<ReqMatch> reqMatchersList) {
        {
            ReqMatch m1 = reqMatchersList.get(0);
            MethodReqMatch mrm = (MethodReqMatch) m1;
            assertEquals("main", mrm.getMethodName());
            assertEquals("[LString", mrm.getArgs().get(0));
            assertEquals(1, mrm.getArgs().size());
        }

        {
            ReqMatch m1 = reqMatchersList.get(1);
            MethodReqMatch mrm = (MethodReqMatch) m1;
            assertEquals("toto", mrm.getMethodName());
            assertEquals("I", mrm.getArgs().get(0));
            assertEquals(1, mrm.getArgs().size());
        }

        {
            ReqMatch m1 = reqMatchersList.get(2);
            MethodReqMatch mrm = (MethodReqMatch) m1;
            assertEquals("toto2", mrm.getMethodName());
            assertEquals("I", mrm.getArgs().get(0));
            assertEquals("LList", mrm.getArgs().get(1));
            assertEquals(2, mrm.getArgs().size());
        }

        {
            ReqMatch m1 = reqMatchersList.get(3);
            MethodReqMatch mrm = (MethodReqMatch) m1;
            assertEquals("toto3", mrm.getMethodName());
            assertEquals(0, mrm.getArgs().size());
        }

        {
            ReqMatch m1 = reqMatchersList.get(4);
            MethodReqMatch mrm = (MethodReqMatch) m1;
            assertEquals("toto4", mrm.getMethodName());
            assertEquals(1, mrm.getArgs().size());
            assertEquals("D", mrm.getArgs().get(0));
        }

        {
            ReqMatch m1 = reqMatchersList.get(5);
            MethodReqMatch mrm = (MethodReqMatch) m1;
            assertEquals("toto5", mrm.getMethodName());
            assertEquals(1, mrm.getArgs().size());
            assertEquals("[D", mrm.getArgs().get(0));
        }

        {
            ReqMatch m1 = reqMatchersList.get(6);
            MethodReqMatch mrm = (MethodReqMatch) m1;
            assertEquals("toto6", mrm.getMethodName());
            assertEquals(1, mrm.getArgs().size());
            assertEquals("[LDouble", mrm.getArgs().get(0));
        }
    }

    @Test
    public void testBlame() throws FileNotFoundException {


        BlameDataWrapper wrapper = new BlameDataWrapper();

        wrapper.blameData.put("toto.A", new HashMap<>());
        for (int i = 0; i < 999; i++) {
            wrapper.blameData.get("toto.A").put(i, new ArrayList<>());
        }
        ReqMatcherJavaVisitor blameVisitor = new ReqMatcherJavaVisitor();

        JavaParser parser = new JavaParser();

        Optional<CompilationUnit> cu = parser.parse(Paths.get("src/test/resources/A5.java").toFile()).getResult();

        cu.get().accept(blameVisitor, wrapper);

        List<ReqMatch> reqMatchersList = new ArrayList<>(blameVisitor.getMatchers());
        reqMatchersList.sort(new Comparator<ReqMatch>() {
            @Override
            public int compare(ReqMatch reqMatch, ReqMatch t1) {
                return ((MethodReqMatch) reqMatch).getMethodName().compareTo(((MethodReqMatch) t1).getMethodName());
            }
        });

        methodSignatureExtraction(reqMatchersList);

    }


}
