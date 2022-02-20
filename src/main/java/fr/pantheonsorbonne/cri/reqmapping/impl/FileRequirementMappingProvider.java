package fr.pantheonsorbonne.cri.reqmapping.impl;

import java.nio.file.Path;
import java.util.Collection;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher;


public interface FileRequirementMappingProvider {
	Collection<ReqMatcher> getReqMatcher(Path p);
}
