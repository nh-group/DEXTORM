package fr.pantheonsorbonne.cri.reqmapping.impl;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;

import java.nio.file.Path;
import java.util.Collection;


public interface FileRequirementMappingProvider {
    Collection<ReqMatch> getReqMatcher(Path p);
}
