package fr.pantheonsorbonne.cri.configuration.model.collectors;

public class GitHubIssueCollectorConfig {
    String gitHubRepoName;
    String repoAddress;

   
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


}
