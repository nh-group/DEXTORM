package fr.pantheonsorbonne.cri.publisher.rest.model;

public class IssueReport {
    Gumtree gumtree;
    GitBlame gitblame;

    public IssueReport(Gumtree gumtree, GitBlame gitblame) {
        this.gumtree = gumtree;
        this.gitblame = gitblame;
    }

    public IssueReport(Gumtree gumtree) {
        this.gumtree = gumtree;
        this.gitblame = new GitBlame(0, 0);
    }

    public Gumtree getGumtree() {
        return gumtree;
    }

    public void setGumtree(Gumtree gumtree) {
        this.gumtree = gumtree;
    }

    public GitBlame getGitblame() {
        return gitblame;
    }

    public void setGitblame(GitBlame gitblame) {
        this.gitblame = gitblame;
    }
}
