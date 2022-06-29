package fr.pantheonsorbonne.cri.app;

import com.github.gumtreediff.client.Run;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import fr.pantheonsorbonne.cri.reqmapping.MethodReqMatchImpl;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.BlameDataWrapper;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.BlameMethodDeclarationVisitor;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.Diff;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFacade;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMethodSignature {

    @Test
    public void testGT() {
        Run.initGenerators();
        CommitFileMaterialization f1 = new CommitFileMaterialization(Paths.get("src/test/resources/A5.java"),
                "commit1");

        List<Diff> diffs = Diff.getBuilder().add(f1.file, f1.commitId).build();
        GumTreeFacade facade = new GumTreeFacade();
        Collection<ReqMatch> reqMatcherImpls = facade.getReqMatcher(diffs, true, false);
        List<MethodReqMatchImpl> reqMatchersListImpl = reqMatcherImpls.stream().map(r -> ((MethodReqMatchImpl) r)).collect(Collectors.toList());
        methodSignatureExtraction(reqMatchersListImpl);

    }

    private void methodSignatureExtraction(List<MethodReqMatchImpl> reqMatchersListImpl) {
        {
            MethodReqMatchImpl mrm = reqMatchersListImpl.get(0);

            assertEquals("main", mrm.getMethodName());
            assertEquals("[LString", mrm.getArgs().get(0));
            assertEquals(1, mrm.getArgs().size());
        }

        {
            MethodReqMatchImpl mrm = reqMatchersListImpl.get(1);

            assertEquals("toto", mrm.getMethodName());
            assertEquals("I", mrm.getArgs().get(0));
            assertEquals(1, mrm.getArgs().size());
        }

        {
            MethodReqMatchImpl mrm = reqMatchersListImpl.get(2);

            assertEquals("toto2", mrm.getMethodName());
            assertEquals("I", mrm.getArgs().get(0));
            assertEquals("LList", mrm.getArgs().get(1));
            assertEquals(2, mrm.getArgs().size());
        }

        {
            MethodReqMatchImpl mrm = reqMatchersListImpl.get(3);

            assertEquals("toto3", mrm.getMethodName());
            assertEquals(0, mrm.getArgs().size());
        }

        {
            MethodReqMatchImpl mrm = reqMatchersListImpl.get(4);

            assertEquals("toto4", mrm.getMethodName());
            assertEquals(1, mrm.getArgs().size());
            assertEquals("D", mrm.getArgs().get(0));
        }

        {
            MethodReqMatchImpl mrm = reqMatchersListImpl.get(5);

            assertEquals("toto5", mrm.getMethodName());
            assertEquals(1, mrm.getArgs().size());
            assertEquals("[D", mrm.getArgs().get(0));
        }

        {
            MethodReqMatchImpl mrm = reqMatchersListImpl.get(6);

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
        BlameMethodDeclarationVisitor blameVisitor = new BlameMethodDeclarationVisitor();

        JavaParser parser = new JavaParser();

        Optional<CompilationUnit> cu = parser.parse(Paths.get("src/test/resources/A5.java").toFile()).getResult();

        cu.get().accept(blameVisitor, wrapper);

        List<MethodReqMatchImpl> reqMatchersListImpl = new ArrayList<>(blameVisitor.getMatchers()).stream().map(r -> ((MethodReqMatchImpl) r)).collect(Collectors.toList());
        reqMatchersListImpl.sort(new Comparator<MethodReqMatchImpl>() {
            @Override
            public int compare(MethodReqMatchImpl t0, MethodReqMatchImpl t1) {
                return t0.getMethodName().compareTo(t1.getMethodName());
            }
        });


        methodSignatureExtraction(reqMatchersListImpl);

    }


}
