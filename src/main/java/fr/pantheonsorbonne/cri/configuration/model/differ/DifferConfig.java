package fr.pantheonsorbonne.cri.configuration.model.differ;

import java.util.Map;

public class DifferConfig {
    public Map<String, DifferAlgorithmConfig> differs;

    public DifferConfig(Map<String, DifferAlgorithmConfig> differs) {
        this.differs = differs;
    }

    public DifferConfig() {
    }

    public Map<String, DifferAlgorithmConfig> getDiffers() {
        return differs;
    }

    public void setDiffers(Map<String, DifferAlgorithmConfig> differs) {
        this.differs = differs;
    }

    @Override
    public String toString() {
        return "DifferConfig{" +
                "differs=" + differs +
                '}';
    }
}
