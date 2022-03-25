package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

import com.google.common.base.Strings;


public class StackTraceParser {


    private final String instrumentedPackage;
    private StackTraceElement[] elements;

    private Collection<ReqMatcher> reqMatchers;

    public StackTraceParser(StackTraceElement[] elements, String instrumentedPackage, Collection<ReqMatcher> reqMatchers) {
        this.elements = elements;
        this.reqMatchers = reqMatchers;
        this.instrumentedPackage = instrumentedPackage;
    }

    private static boolean match(StackTraceElement elt, ReqMatcher m) {
        return m.getFQClassName().equals(elt.getClassName().replaceAll("/", "."))
                && m.getMethodName().equals(elt.getMethodName().split("\\$")[0])
                && m.getReq().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));

    }

    public Collection<String> getReqs() {

        Collection<String> res = new HashSet<>();
        for (StackTraceElement elt : elements) {
            if (elt.getClassName().replaceAll("/", ".").startsWith(instrumentedPackage.replaceAll("/", "."))) {

                for (ReqMatcher m : reqMatchers) {
                    if (match(elt, m)) {
                        res.addAll(m.getReq());
                    }
                }

            }
        }
        return res;

    }
}
