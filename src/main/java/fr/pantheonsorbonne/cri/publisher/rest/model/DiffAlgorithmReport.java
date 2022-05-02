package fr.pantheonsorbonne.cri.publisher.rest.model;

public class DiffAlgorithmReport {
    int lineCoverage = 0;
    int methodCoverage = 0;

    public DiffAlgorithmReport(int lineCoverage, int methodCoverage) {
        this.lineCoverage = lineCoverage;
        this.methodCoverage = methodCoverage;
    }

    public int getLineCoverage() {
        return lineCoverage;
    }

    public void setLineCoverage(int lineCoverage) {
        this.lineCoverage = lineCoverage;
    }

    public int getMethodCoverage() {
        return methodCoverage;
    }
}
