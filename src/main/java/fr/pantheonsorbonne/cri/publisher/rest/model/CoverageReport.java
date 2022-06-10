package fr.pantheonsorbonne.cri.publisher.rest.model;

public class CoverageReport {
    String projectName;
    CoverageProjectReport coverageProjectReport;

    public CoverageReport(String projectName, CoverageProjectReport coverageProjectReport) {
        this.projectName = projectName;
        this.coverageProjectReport = coverageProjectReport;
    }

    public CoverageReport() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public CoverageProjectReport getCoverageProjectReport() {
        return coverageProjectReport;
    }

    public void setCoverageProjectReport(CoverageProjectReport coverageProjectReport) {
        this.coverageProjectReport = coverageProjectReport;
    }
}
