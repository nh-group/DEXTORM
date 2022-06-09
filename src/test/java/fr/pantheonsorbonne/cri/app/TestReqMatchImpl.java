package fr.pantheonsorbonne.cri.app;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.StackTraceElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestReqMatchImpl {

    @Test
    void test() {


        {
            StackTraceElement ste = new StackTraceElement("A.java", "toto", "A", "main", "([LString;)i'", 10);
            ReqMatch rmatch = ReqMatch.builder().className("A").packageName("toto").args("([LString;)i").methodName("main").line(10).commit("1").build();
            Assertions.assertTrue(rmatch.isMatch(ste));
        }

        {
            StackTraceElement ste = new StackTraceElement("A.java", "toto", "A", "main", "([LString;)i'", 10);
            ReqMatch rmatch = ReqMatch.builder().className("A").packageName("toto").args("[LString").methodName("main").line(10).commit("1").build();
            Assertions.assertTrue(rmatch.isMatch(ste));
        }

        {
            StackTraceElement ste = new StackTraceElement("A.java", "toto", "A", "main", "([LString;D;I;)V'", 10);
            ReqMatch rmatch = ReqMatch.builder().className("A").packageName("toto").arg("[LString").arg("D").arg("I").methodName("main").line(10).commit("1").build();
            Assertions.assertTrue(rmatch.isMatch(ste));
        }

        {
            StackTraceElement ste = new StackTraceElement("A.java", "toto", "A", "main2", "([LString;D;I;)V'", 10);
            ReqMatch rmatch = ReqMatch.builder().className("A").packageName("toto").arg("[LString").arg("D").arg("I").methodName("main").line(10).commit("1").build();
            Assertions.assertFalse(rmatch.isMatch(ste));
        }

        {
            StackTraceElement ste1 = new StackTraceElement("A.java", "toto", "A", "main", "([LString;D;J;)V'", 10);
            StackTraceElement ste2 = new StackTraceElement("A.java", "toto", "toto.A", "main", "([LString;D;D;)V'", 10);
            ReqMatch rmatch = ReqMatch.builder().className("A").packageName("toto").arg("[LString").arg("D").arg("I").methodName("main").line(10).commit("1").build();
            Assertions.assertFalse(rmatch.isMatch(ste1));
            Assertions.assertFalse(rmatch.isMatch(ste2));
        }


    }
}
