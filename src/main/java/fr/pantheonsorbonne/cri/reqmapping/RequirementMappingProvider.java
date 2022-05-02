package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Set;

public interface RequirementMappingProvider {

    Set<ReqMatch> getReqMatcher();

    int countReqMatchers();

}