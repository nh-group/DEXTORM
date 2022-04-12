package fr.pantheonsorbonne.cri.configuration.model;

import java.util.Map;

public class PublisherConfig {
    Map<String, GrpcPublisherConfig> grpcPublishers;
    Map<String, ConsolePublisherConfig> consolePublishers;

    public Map<String, RESTPublisherConfig> getRestPublishers() {
        return restPublishers;
    }

    public void setRestPublishers(Map<String, RESTPublisherConfig> restPublishers) {
        this.restPublishers = restPublishers;
    }

    Map<String, RESTPublisherConfig> restPublishers;

    public Map<String, DBPublisherConfig> getDbPublishers() {
        return dbPublishers;
    }

    public void setDbPublishers(Map<String, DBPublisherConfig> dbPublishers) {
        this.dbPublishers = dbPublishers;
    }

    Map<String, DBPublisherConfig> dbPublishers;

    public PublisherConfig(Map<String, GrpcPublisherConfig> grpcPublishers, Map<String, ConsolePublisherConfig> consolePublishers, Map<String, DBPublisherConfig> dbPublishers, Map<String, RESTPublisherConfig> restPublishers) {
        this.grpcPublishers = grpcPublishers;
        this.consolePublishers = consolePublishers;
        this.dbPublishers = dbPublishers;
        this.restPublishers = restPublishers;
    }

    public PublisherConfig() {
    }

    public Map<String, GrpcPublisherConfig> getGrpcPublishers() {
        return grpcPublishers;
    }

    public void setGrpcPublishers(Map<String, GrpcPublisherConfig> grpcPublishers) {
        this.grpcPublishers = grpcPublishers;
    }

    public Map<String, ConsolePublisherConfig> getConsolePublishers() {
        return consolePublishers;
    }

    public void setConsolePublishers(Map<String, ConsolePublisherConfig> consolePublishers) {
        this.consolePublishers = consolePublishers;
    }

    @Override
    public String toString() {
        return "PublisherConfig{" +
                "grpcPublishers=" + grpcPublishers +
                ", consolePublishers=" + consolePublishers +
                '}';
    }
}
