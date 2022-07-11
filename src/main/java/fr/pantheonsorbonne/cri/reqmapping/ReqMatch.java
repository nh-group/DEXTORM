package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Collection;

public interface ReqMatch {
    static ReqMatcherBuilder builder() {
        return new ReqMatcherBuilder();
    }

    boolean isMatch(StackTraceElement elt);

    Collection<String> getRequirementsIds();

    Collection<StackTraceElement> getMatchingTraceElement();

    String getFQClassName();
}
