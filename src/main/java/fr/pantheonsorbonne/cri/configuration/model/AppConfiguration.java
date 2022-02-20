package fr.pantheonsorbonne.cri.configuration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppConfiguration {
    @JsonProperty
    String instrumentedPackage;
    @JsonProperty
    String sourceRootDir;
    @JsonProperty("publisher")
    String publisherName;
    @JsonProperty("issueCollector")
    String issueCollectorName;
    @JsonProperty("diffAlgorithm")
    String diffAlgorithmName;

    @Override
    public String toString() {
        return "AppConfiguration{" +
                "instrumentedPackage='" + instrumentedPackage + '\'' +
                ", sourceRootDir='" + sourceRootDir + '\'' +
                ", publisherName='" + publisherName + '\'' +
                ", issueCollectorName='" + issueCollectorName + '\'' +
                ", diffAlgorithmName='" + diffAlgorithmName + '\'' +
                '}';
    }

    public String getInstrumentedPackage() {
        return instrumentedPackage;
    }

    public void setInstrumentedPackage(String instrumentedPackage) {
        this.instrumentedPackage = instrumentedPackage;
    }

    public String getSourceRootDir() {
        return sourceRootDir;
    }

    public void setSourceRootDir(String sourceRootDir) {
        this.sourceRootDir = sourceRootDir;
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
}
