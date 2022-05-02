package fr.pantheonsorbonne.cri.configuration.model.publisher;

public class DBPublisherConfig {

    private String jdbc_path;

    public DBPublisherConfig(String jdbc_path) {
        this.jdbc_path = jdbc_path;
    }

    public DBPublisherConfig() {
    }

    public String getJdbc_path() {
        return jdbc_path;
    }

    public void setJdbc_path(String jdbc_path) {
        this.jdbc_path = jdbc_path;
    }
}
