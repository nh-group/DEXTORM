package fr.pantheonsorbonne.cri.configuration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AppConfiguration {
    @JsonProperty
    String instrumentedPackage;
    @JsonProperty
    List<String> sourceRootDirs;
    @JsonProperty("publisher")
    String publisherName;
    @JsonProperty("issueCollector")
    String issueCollectorName;
    @JsonProperty("diffAlgorithm")
    String diffAlgorithmName;
    @JsonProperty("coverageFolder")
    String coverageFolder;

    public String getIdeToolExportPath() {
        return ideToolExportPath;
    }

    public void setIdeToolExportPath(String ideToolExportPath) {
        this.ideToolExportPath = ideToolExportPath;
    }

    @JsonProperty(value = "ideToolExportPath",defaultValue = "")
    String ideToolExportPath="";

    public String getCoverageFolder() {
        return coverageFolder;
    }

    public void setCoverageFolder(String coverageFolder) {
        this.coverageFolder = coverageFolder;
    }

    public String getInstrumentedPackage() {
        return instrumentedPackage;
    }

    public void setInstrumentedPackage(String instrumentedPackage) {
        this.instrumentedPackage = instrumentedPackage;
    }

    public List<String> getSourceRootDirs() {
        return sourceRootDirs;
    }

    public void setSourceRootDirs(List<String> sourceRootDirs) {
        this.sourceRootDirs = sourceRootDirs;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getIssueCollectorName() {
        return issueCollectorName;
    }

    public void setIssueCollectorName(String issueCollectorName) {
        this.issueCollectorName = issueCollectorName;
    }

    public String getDiffAlgorithmName() {
        return diffAlgorithmName;
    }

    public void setDiffAlgorithmName(String diffAlgorithmName) {
        this.diffAlgorithmName = diffAlgorithmName;
    }


    @Override
    public String toString() {
        return "AppConfiguration{" +
                "instrumentedPackage='" + instrumentedPackage + '\'' +
                ", sourceRootDirs=" + sourceRootDirs +
                ", publisherName='" + publisherName + '\'' +
                ", issueCollectorName='" + issueCollectorName + '\'' +
                ", diffAlgorithmName='" + diffAlgorithmName + '\'' +
                ", coverageFolder='" + coverageFolder + '\'' +
                ", ideToolExportPath='" + ideToolExportPath + '\'' +
                '}';
    }
}
