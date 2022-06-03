package fr.pantheonsorbonne.cri.configuration.model.collectors;

public class GitHubIssueCollectorConfig {
    String gitHubRepoName;
    String repoAddress;
    String branch;

    public GitHubIssueCollectorConfig() {
    }

    public String getRepoAddress() {
        if (repoAddress == null) {
            return "https://github.com/" + gitHubRepoName + ".git";
        }
        return repoAddress;
    }

    public void setRepoAddress(String repoAddress) {
        this.repoAddress = repoAddress;
    }

    public String getGitHubRepoName() {
        return gitHubRepoName;
    }

    public void setGitHubRepoName(String gitHubRepoName) {
        this.gitHubRepoName = gitHubRepoName;
    }

    public String getBranch() {
        if (branch != null) {
            return branch;
        } else {
            return "master";
        }
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
