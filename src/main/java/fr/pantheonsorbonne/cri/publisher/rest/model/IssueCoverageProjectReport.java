package fr.pantheonsorbonne.cri.publisher.rest.model;

import java.util.HashMap;
import java.util.Map;

public class IssueCoverageProjectReport {
    Map<String, Double> DataIssueConverageProjectReport = new HashMap<>();

    public IssueCoverageProjectReport(Map<String, Double> dataIssueConverageProjectReport) {
        DataIssueConverageProjectReport = dataIssueConverageProjectReport;
    }

    public Map<String, Double> getDataIssueConverageProjectReport() {
        return DataIssueConverageProjectReport;
    }

    public void setDataIssueConverageProjectReport(Map<String, Double> dataIssueConverageProjectReport) {
        DataIssueConverageProjectReport = dataIssueConverageProjectReport;
    }
}
