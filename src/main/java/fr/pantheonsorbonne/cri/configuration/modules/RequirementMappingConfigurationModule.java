package fr.pantheonsorbonne.cri.configuration.modules;

import com.github.gumtreediff.client.Run;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.configuration.variables.DiffAlgorithm;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.FileRequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.GitBlameFileRequirementProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.blame.GitRepoRequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.GumTreeFileRequirementMappingProvider;

public class RequirementMappingConfigurationModule extends AbstractModule {

    private final DiffAlgorithm algo;
    private final boolean methods;
    private final boolean instructions;
    private final Class<? extends RequirementIssueDecorator> requirementIssueDecorator;

    public RequirementMappingConfigurationModule(DiffAlgorithm algo, boolean methods, boolean instructions, Class<? extends RequirementIssueDecorator> requirementIssueDecorator) {
        this.algo = algo;
        this.methods = methods;
        this.instructions = instructions;
        this.requirementIssueDecorator = requirementIssueDecorator;
    }

    @Provides
    @Named("diffMethod")
    public String getdiffMethod() {
        return this.algo.getConfName();
    }


    @Override
    protected void configure() {

        super.configure();

        bind(RequirementMappingProvider.class).to(GitRepoRequirementMappingProvider.class);
        bind(RequirementIssueDecorator.class).to(requirementIssueDecorator);
        bind(Boolean.class).annotatedWith(Names.named("DoMethodsDiff")).toInstance(Boolean.valueOf(this.methods));
        bind(Boolean.class).annotatedWith(Names.named("DoInstructionsDiff")).toInstance(Boolean.valueOf(this.instructions));

        switch (algo) {
            case BLAME:
                bind(FileRequirementMappingProvider.class).to(GitBlameFileRequirementProvider.class);
                break;
            case GUMTREE:
                bind(FileRequirementMappingProvider.class).to(GumTreeFileRequirementMappingProvider.class);
                Run.initGenerators();
                break;
            default:
                throw new RuntimeException("Fail to load diffalgorithm, check your configuration file");
        }

        //

    }
}
