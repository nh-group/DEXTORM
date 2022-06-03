package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Collection;

public interface ReqMatch {
    boolean isMatch(StackTraceElement elt);

    Collection<String> getRequirementsIds();

    Collection<StackTraceElement> getMatchingTraceElement();

}
