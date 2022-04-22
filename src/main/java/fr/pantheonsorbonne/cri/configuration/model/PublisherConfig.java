package fr.pantheonsorbonne.cri.configuration.model;

import java.util.Map;

public class PublisherConfig {
    Map<String, GrpcPublisherConfig> grpcPublishers;
    Map<String, FilePublisherConfig> filePublishers;
    Map<String, RESTPublisherConfig> restPublishers;
    Map<String, DBPublisherConfig> dbPublishers;

    public PublisherConfig(Map<String, GrpcPublisherConfig> grpcPublishers, Map<String, FilePublisherConfig> consolePublishers, Map<String, DBPublisherConfig> dbPublishers, Map<String, RESTPublisherConfig> restPublishers) {
        this.grpcPublishers = grpcPublishers;
        this.filePublishers = consolePublishers;
        this.dbPublishers = dbPublishers;
        this.restPublishers = restPublishers;
    }

    public PublisherConfig() {
    }

    public Map<String, RESTPublisherConfig> getRestPublishers() {
        return restPublishers;
    }

    public void setRestPublishers(Map<String, RESTPublisherConfig> restPublishers) {
        this.restPublishers = restPublishers;
    }

    public Map<String, DBPublisherConfig> getDbPublishers() {
        return dbPublishers;
    }

    public void setDbPublishers(Map<String, DBPublisherConfig> dbPublishers) {
        this.dbPublishers = dbPublishers;
    }

    public Map<String, GrpcPublisherConfig> getGrpcPublishers() {
        return grpcPublishers;
    }

    public void setGrpcPublishers(Map<String, GrpcPublisherConfig> grpcPublishers) {
        this.grpcPublishers = grpcPublishers;
    }

    public Map<String, FilePublisherConfig> getFilePublishers() {
        return filePublishers;
    }

    public void setFilePublishers(Map<String, FilePublisherConfig> filePublishers) {
        this.filePublishers = filePublishers;
    }

    @Override
    public String toString() {
        return "PublisherConfig{" +
                "grpcPublishers=" + grpcPublishers +
                ", consolePublishers=" + filePublishers +
                '}';
    }
}
