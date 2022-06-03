package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.reqmapping.GitHubRequirementIssueDecorator;

public class GitHubRepoProviderModule extends GitRepoProviderModule {


    public GitHubRepoProviderModule(String gitHubRepoName, String repoAddress, String branch) {
        super(gitHubRepoName, repoAddress, branch);
    }

    @Override
    protected Class<? extends RequirementIssueDecorator> getInternalRequirementIssueDecorator() {
        return GitHubRequirementIssueDecorator.class;
    }

    @Provides
    @Named("gitHubRepoName")
    public String getIssueCollectorAddress() {
        return this.issueCollectorAddress;
    }
}
