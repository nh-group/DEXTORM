package fr.pantheonsorbonne.cri.mapping.impl;

import java.nio.file.Path;
import java.util.Collection;

import com.google.inject.ImplementedBy;
import com.google.inject.ProvidedBy;

import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.impl.diff.BlameVisitorAdaptor;

@ImplementedBy(BlameVisitorAdaptor.class)
public interface FileRequirementMappingProvider {
	Collection<ReqMatcher> getReqMatcher(Path p);
}
