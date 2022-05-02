package fr.pantheonsorbonne.cri.configuration.model.publisher;

public class FilePublisherConfig {
    private String filePath;

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


}
