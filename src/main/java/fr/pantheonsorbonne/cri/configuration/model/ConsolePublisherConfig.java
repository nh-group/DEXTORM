package fr.pantheonsorbonne.cri.configuration.model;

import java.util.Collection;

public class ConsolePublisherConfig {
    public ConsolePublisherConfig(String filePath) {
        this.filePath = filePath;
    }

    public ConsolePublisherConfig() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String filePath;


}
