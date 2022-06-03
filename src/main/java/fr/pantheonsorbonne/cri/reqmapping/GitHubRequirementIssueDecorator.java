package fr.pantheonsorbonne.cri.reqmapping;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class GitHubRequirementIssueDecorator implements RequirementIssueDecorator {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(GitHubRequirementIssueDecorator.class);
    String gitHubRepoName;

    @Inject
    public GitHubRequirementIssueDecorator(@Named("gitHubRepoName") String gitHubRepoName) {
        this.gitHubRepoName = gitHubRepoName;
    }

    @Override
    public Requirement getIssueLink(Requirement req) {
        try {
            Integer issue = Integer.parseInt(req.getId());
            return Requirement.newBuilder(req).setIssueURI(URI.create("https://github.com/" + gitHubRepoName + "/" + issue).toString()).build();
        } catch (java.lang.NumberFormatException e) {
            LOG.warn("failed to find github issue for req" + req);
            return req;
        }

    }

    @Override
    public String getRoot() {
        return this.gitHubRepoName;
    }

}
