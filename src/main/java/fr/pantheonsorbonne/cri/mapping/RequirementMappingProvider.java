package fr.pantheonsorbonne.cri.mapping;

import java.util.Collection;

public interface RequirementMappingProvider {
	public Collection<ReqMatcher> getReqMatcher();
}
