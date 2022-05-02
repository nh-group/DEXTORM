package fr.pantheonsorbonne.cri.app;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcherBuilder;
import fr.pantheonsorbonne.cri.reqmapping.StackTraceElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestReqMatch {

    @Test
    void test() {

        StackTraceElement ste = new StackTraceElement("A.java", "toto", "toto.A", "main", "([LString;)i'", 10);
        ReqMatch rmatch = new ReqMatcherBuilder().className("A").packageName("toto").args("([LString;)i").methodName("main").line(10).commit("1").build();
        Assertions.assertTrue(rmatch.isMatch(ste));


    }
}
