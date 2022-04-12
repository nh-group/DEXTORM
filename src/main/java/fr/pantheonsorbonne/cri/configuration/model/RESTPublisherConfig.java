package fr.pantheonsorbonne.cri.configuration.model;

public class RESTPublisherConfig {

    private String baseUrl;

    public RESTPublisherConfig() {
    }

    public RESTPublisherConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
