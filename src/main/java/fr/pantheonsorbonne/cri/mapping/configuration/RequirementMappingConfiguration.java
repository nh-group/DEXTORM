package fr.pantheonsorbonne.cri.mapping.configuration;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters.DiffAlgorithm;
import fr.pantheonsorbonne.cri.mapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.FileRequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.blame.GitBlameFileRequirementProvider;
import fr.pantheonsorbonne.cri.mapping.impl.blame.GitRepoRequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFileRequirementMappingProvider;

public class RequirementMappingConfiguration extends AbstractModule {

	private DiffAlgorithm algo;

	public RequirementMappingConfiguration(ApplicationParameters.DiffAlgorithm algo) {
		this.algo = algo;
	}

	@Override
	protected void configure() {

		super.configure();

		bind(RequirementMappingProvider.class).to(GitRepoRequirementMappingProvider.class);

		switch (algo) {
		case BLAME:
			bind(FileRequirementMappingProvider.class).to(GitBlameFileRequirementProvider.class);
			break;
		case GUMTREE:
			bind(FileRequirementMappingProvider.class).to(GumTreeFileRequirementMappingProvider.class);
			break;
		default:
			throw new RuntimeException("Fail to load diffalgorithm, check your configuration file");
		}

		//

	}
}
