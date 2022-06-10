package fr.pantheonsorbonne.cri.configuration.model.publisher;

import java.util.Map;

public class PublisherConfig {
    public Map<String, GrpcPublisherConfig> grpcPublishers;
    public Map<String, FilePublisherConfig> filePublishers;
    public Map<String, RESTPublisherConfig> restPublishers;


    public PublisherConfig(Map<String, GrpcPublisherConfig> grpcPublishers, Map<String, FilePublisherConfig> consolePublishers, Map<String, RESTPublisherConfig> restPublishers) {
        this.grpcPublishers = grpcPublishers;
        this.filePublishers = consolePublishers;
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
