package fr.pantheonsorbonne.cri.mapping.configuration;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.mapping.RepoRequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.diff.GitBlameRequirementProvider;

public class RequirementMappingConfiguration extends AbstractModule {
	
	
	@Override
	protected void configure() {

		super.configure();
		
		

		//bind(RequirementMappingProvider.class).to(GitBlameRequirementProvider.class);
		bind(RepoRequirementMappingProvider.class).to(GitBlameRequirementProvider.class);
		

	}
}
