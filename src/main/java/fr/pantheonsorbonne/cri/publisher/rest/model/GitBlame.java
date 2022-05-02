package fr.pantheonsorbonne.cri.publisher.rest.model;

public class GitBlame extends DiffAlgorithmReport {
    public GitBlame(int lineCoverage, int methodCoverage) {
        super(lineCoverage, methodCoverage);
    }
}
