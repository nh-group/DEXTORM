package fr.pantheonsorbonne.cri.publisher.rest.model;

import java.util.HashMap;
import java.util.Map;

public class CoverageProjectReport {
    long timestamp;
    Map<String, IssueCoverageProjectReport> issueCoverageProjectReport = new HashMap<>();

    public CoverageProjectReport() {
    }

    public CoverageProjectReport(long timestamp, Map<String, IssueCoverageProjectReport> issueCoverageProjectReport) {
        this.timestamp = timestamp;
        this.issueCoverageProjectReport = issueCoverageProjectReport;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, IssueCoverageProjectReport> getIssueCoverageProjectReport() {
        return issueCoverageProjectReport;
    }

    public void setIssueCoverageProjectReport(Map<String, IssueCoverageProjectReport> issueCoverageProjectReport) {
        this.issueCoverageProjectReport = issueCoverageProjectReport;
    }
}
