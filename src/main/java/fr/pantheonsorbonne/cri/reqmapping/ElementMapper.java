package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ElementMapper {


    private final String instrumentedPackage;
    private final StackTraceElement[] elements;

    private final Set<ReqMatch> reqMatcherImpls;

    public ElementMapper(StackTraceElement[] elements, String instrumentedPackage, Set<ReqMatch> reqMatcherImpls) {
        this.elements = elements;
        this.reqMatcherImpls = reqMatcherImpls;
        this.instrumentedPackage = instrumentedPackage;
    }

    public Set<String> getMatchingRequirementsIdSet() {

        return this.getMatchingRequirementsMatchers().stream().map(rm -> rm.getRequirementsIds()).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public Set<ReqMatch> getMatchingRequirementsMatchers() {

        Set<ReqMatch> res = new HashSet<>();
        for (StackTraceElement elt : elements) {
            if (elt.getPackageName().startsWith(this.instrumentedPackage)) {
                for (ReqMatch m : reqMatcherImpls) {
                    if (match(elt, m)) {
                        //System.out.println(elt + " ->" + m);
                        res.add(m);
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

    public List<String> getMatchingRequirementsIdList() {

        return this.getMatchingRequirementsMatchers().stream().map(rm -> rm.getRequirementsIds()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Set<StackTraceElement> getMatchedElements() {
        return this.getMatchingRequirementsMatchers().stream().map(r -> r.getMatchingTraceElement()).flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
