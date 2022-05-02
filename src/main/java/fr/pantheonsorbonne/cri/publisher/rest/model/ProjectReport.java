package fr.pantheonsorbonne.cri.publisher.rest.model;

import java.util.Map;

public class ProjectReport {

    Map<String, Issue> issues;


    public Map<String, Issue> getIssues() {
        return issues;
    }

    public void setIssues(Map<String, Issue> issues) {
        this.issues = issues;
    }
}
