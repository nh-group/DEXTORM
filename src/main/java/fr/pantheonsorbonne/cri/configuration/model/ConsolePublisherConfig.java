package fr.pantheonsorbonne.cri.configuration.model;

import java.util.Collection;

public class ConsolePublisherConfig {
    Collection<String> data;

    @Override
    public String toString() {
        return "ConsolePublisherConfig{" +
                "data=" + data +
                '}';
    }
}
