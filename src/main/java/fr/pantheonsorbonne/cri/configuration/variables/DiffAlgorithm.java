package fr.pantheonsorbonne.cri.configuration.variables;

public enum DiffAlgorithm {
    GUMTREE("gumtree"), BLAME("gitblame");
    String confName;

    DiffAlgorithm(String confName) {
        this.confName = confName;
    }

    public String getConfName() {
        return this.confName;
    }


}
