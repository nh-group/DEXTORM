package fr.pantheonsorbonne.cri.reqmapping.impl;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher;

import java.nio.file.Path;
import java.util.Collection;


public interface FileRequirementMappingProvider {
    Collection<ReqMatcher> getReqMatcher(Path p);
}
