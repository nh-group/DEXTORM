package fr.pantheonsorbonne.cri.configuration.model;

public class GitHubIssueCollectorConfig {
    String repo;

    public GitHubIssueCollectorConfig(String repo) {
        this.repo = repo;
    }

    public GitHubIssueCollectorConfig() {
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    @Override
    public String toString() {
        return "GitHubIssueCollectorConfig{" +
                "repo='" + repo + '\'' +
                '}';
    }
}
