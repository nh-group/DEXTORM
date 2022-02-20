package fr.pantheonsorbonne.cri.configuration.model;

public class GrpcPublisherConfig {
    String host;
    int port;

    public GrpcPublisherConfig(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public GrpcPublisherConfig() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "GrpcPublisherConfig{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
