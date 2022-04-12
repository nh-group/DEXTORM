package fr.pantheonsorbonne.cri.configuration.model;

import java.util.Collection;

public class FilePublisherConfig {
    public FilePublisherConfig(String filePath) {
        this.filePath = filePath;
    }

    public FilePublisherConfig() {
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String filePath;


}
