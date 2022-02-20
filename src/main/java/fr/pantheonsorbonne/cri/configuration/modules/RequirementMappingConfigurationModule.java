package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.configuration.variables.DiffAlgorithm;
import fr.pantheonsorbonne.cri.reqmapping.GitHubRequirementIssueDecorator;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.FileRequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.GitBlameFileRequirementProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.GitRepoRequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFileRequirementMappingProvider;

public class RequirementMappingConfigurationModule extends AbstractModule {

    private final DiffAlgorithm algo;
    private final Class<? extends RequirementIssueDecorator> requirementIssueDecorator;

    public RequirementMappingConfigurationModule(DiffAlgorithm algo, Class<? extends RequirementIssueDecorator> requirementIssueDecorator) {
        this.algo = algo;
        this.requirementIssueDecorator = requirementIssueDecorator;
    }


    @Override
    protected void configure() {

        super.configure();

        bind(RequirementMappingProvider.class).to(GitRepoRequirementMappingProvider.class);
        bind(RequirementIssueDecorator.class).to(requirementIssueDecorator);

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