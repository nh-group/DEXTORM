package fr.pantheonsorbonne.cri.publisher.rest.model;

import java.util.Map;

public class Issue {

    Map<Integer, IssueReport> issues;

    public Issue(Map<Integer, IssueReport> issues) {
        this.issues = issues;
    }

    public Map<Integer, IssueReport> getIssues() {
        return issues;
    }

    public void setIssues(Map<Integer, IssueReport> issues) {
        this.issues = issues;
    }
}
