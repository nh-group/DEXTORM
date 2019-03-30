package fr.pantheonsorbonne.cri.mapping.impl;

import java.nio.file.Path;
import java.util.Collection;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;


public interface FileRequirementMappingProvider {
	Collection<ReqMatcher> getReqMatcher(Path p);
}
