package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Collection;
import java.util.HashSet;


public class StackTraceParser {


    private final String instrumentedPackage;
    private final StackTraceElement[] elements;

    private final Collection<ReqMatch> reqMatchers;

    public StackTraceParser(StackTraceElement[] elements, String instrumentedPackage, Collection<ReqMatch> reqMatchers) {
        this.elements = elements;
        this.reqMatchers = reqMatchers;
        this.instrumentedPackage = instrumentedPackage;
    }

    public Collection<String> getReqs() {

        Collection<String> res = new HashSet<>();
        for (StackTraceElement elt : elements) {
            if (elt.getClassName().replaceAll("/", ".").startsWith(instrumentedPackage.replaceAll("/", "."))) {

                for (ReqMatch m : reqMatchers) {
                    if (match(elt, m)) {
                        res.addAll(m.getReq());
                    }
                }

            }
        }
        return res;

    }

    private static boolean match(StackTraceElement elt, ReqMatch m) {
        return m.isMatch(elt);

    }
}
