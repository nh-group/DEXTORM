package fr.pantheonsorbonne.cri.configuration.model;

import java.util.Map;

public class PublisherConfig {
    Map<String, GrpcPublisherConfig> grpcPublishers;
    Map<String, ConsolePublisherConfig> consolePublishers;

    public PublisherConfig(Map<String, GrpcPublisherConfig> grpcPublishers, Map<String, ConsolePublisherConfig> consolePublishers) {
        this.grpcPublishers = grpcPublishers;
        this.consolePublishers = consolePublishers;
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
