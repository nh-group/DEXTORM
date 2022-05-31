package fr.pantheonsorbonne.cri.reqmapping;

import java.util.HashSet;
import java.util.Set;


public class StackTraceParser {


    private final String instrumentedPackage;
    private final StackTraceElement[] elements;

    private final Set<ReqMatch> reqMatcherImpls;

    public StackTraceParser(StackTraceElement[] elements, String instrumentedPackage, Set<ReqMatch> reqMatcherImpls) {
        this.elements = elements;
        this.reqMatcherImpls = reqMatcherImpls;
        this.instrumentedPackage = instrumentedPackage;
    }

    public Set<String> getReqs() {

        Set<String> res = new HashSet<>();
        for (StackTraceElement elt : elements) {
            //System.out.println(elt);
            if (elt.getClassName().replaceAll("/", ".").startsWith(instrumentedPackage.replaceAll("/", "."))) {
                for (ReqMatch m : reqMatcherImpls) {
                    if (match(elt, m)) {
                        res.addAll(m.getReq());
                    }
                }
            }
        }
        return res;
    }

    private static boolean match(StackTraceElement elt, ReqMatch m) {
        //System.out.println(m.toString());
        return m.isMatch(elt);
    }
}
