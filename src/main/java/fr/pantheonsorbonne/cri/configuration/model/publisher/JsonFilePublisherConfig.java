package fr.pantheonsorbonne.cri.configuration.model.publisher;

public class JsonFilePublisherConfig {
    String targetDir;

    public JsonFilePublisherConfig() {
    }

    public JsonFilePublisherConfig(String targetDir) {
        this.targetDir = targetDir;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }
}
