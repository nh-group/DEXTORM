package fr.pantheonsorbonne.cri.scm.issues;

public abstract class AbstractIssueCollector implements IssueCollector {
    protected String collectorName;

    public AbstractIssueCollector(String collectorName) {
        this.collectorName = collectorName;
    }

}
