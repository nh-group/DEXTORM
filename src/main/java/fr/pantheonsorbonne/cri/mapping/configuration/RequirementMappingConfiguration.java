package fr.pantheonsorbonne.cri.mapping.configuration;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.mapping.GitHubRequirementIssueDecorator;
import fr.pantheonsorbonne.cri.mapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.diff.RepoFileVisitor;

public class RequirementMappingConfiguration extends AbstractModule {
	
	
	@Override
	protected void configure() {

		super.configure();

		bind(RequirementMappingProvider.class).to(RepoFileVisitor.class);
		

	}
}
