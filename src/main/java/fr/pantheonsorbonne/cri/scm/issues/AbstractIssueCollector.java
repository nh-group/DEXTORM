package fr.pantheonsorbonne.cri.scm.issues;

public abstract class AbstractIssueCollector implements IssueCollector {
    protected String repo;

    public AbstractIssueCollector(String repo) {
        this.repo = repo;
    }

}
