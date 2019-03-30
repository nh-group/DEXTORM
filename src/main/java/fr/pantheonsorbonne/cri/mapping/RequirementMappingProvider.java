package fr.pantheonsorbonne.cri.mapping;

import java.nio.file.Path;
import java.util.Collection;

public interface RequirementMappingProvider {

	Collection<ReqMatcher> getReqMatcher();

}