package fr.pantheonsorbonne.cri.configuration.model;

import java.util.Map;

public class IssueCollectorsConfig {
    Map<String, GitHubIssueCollectorConfig> github;

    public IssueCollectorsConfig() {
    }

    public Map<String, GitHubIssueCollectorConfig> getGithub() {
        return github;
    }

    public void setGithub(Map<String, GitHubIssueCollectorConfig> github) {
        this.github = github;
    }

    @Override
    public String toString() {
        return "IssueCollectorsConfig{" +
                "github=" + github +
                '}';
    }
}
