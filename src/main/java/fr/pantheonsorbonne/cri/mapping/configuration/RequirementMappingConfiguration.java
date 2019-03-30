package fr.pantheonsorbonne.cri.mapping.configuration;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.mapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.FileRequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.diff.GitBlameFileRequirementProvider;
import fr.pantheonsorbonne.cri.mapping.impl.diff.GitRepoRequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFileRequirementMappingProvider;

public class RequirementMappingConfiguration extends AbstractModule {

	@Override
	protected void configure() {

		super.configure();

		
		bind(RequirementMappingProvider.class).to(GitRepoRequirementMappingProvider.class);
		bind(FileRequirementMappingProvider.class).to(GumTreeFileRequirementMappingProvider.class);

	}
}
