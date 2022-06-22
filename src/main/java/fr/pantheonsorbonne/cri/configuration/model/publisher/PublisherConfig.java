package fr.pantheonsorbonne.cri.configuration.model.publisher;

import java.util.Map;

public class PublisherConfig {
    public Map<String, GrpcPublisherConfig> grpcPublishers;
    public Map<String, FilePublisherConfig> filePublishers;
    public Map<String, RESTPublisherConfig> restPublishers;
    public Map<String, JsonFilePublisherConfig> jsonFilePublisher;

    public PublisherConfig() {
    }

    public PublisherConfig(Map<String, GrpcPublisherConfig> grpcPublishers, Map<String, FilePublisherConfig> filePublishers, Map<String, RESTPublisherConfig> restPublishers, Map<String, JsonFilePublisherConfig> jsonFilePublisher) {
        this.grpcPublishers = grpcPublishers;
        this.filePublishers = filePublishers;
        this.restPublishers = restPublishers;
        this.jsonFilePublisher = jsonFilePublisher;
    }

    public Map<String, JsonFilePublisherConfig> getJsonFilePublisher() {
        return jsonFilePublisher;
    }

    public void setJsonFilePublisher(Map<String, JsonFilePublisherConfig> jsonFilePublisher) {
        this.jsonFilePublisher = jsonFilePublisher;
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
                ", filePublishers=" + filePublishers +
                ", restPublishers=" + restPublishers +
                ", jsonFilePublisher=" + jsonFilePublisher +
                '}';
    }
}
