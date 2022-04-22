package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Collection;

public interface RequirementMappingProvider {

    Collection<ReqMatch> getReqMatcher();

}