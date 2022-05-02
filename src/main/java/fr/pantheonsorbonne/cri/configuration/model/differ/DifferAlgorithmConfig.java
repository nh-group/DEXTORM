package fr.pantheonsorbonne.cri.configuration.model.differ;

import fr.pantheonsorbonne.cri.configuration.variables.DiffAlgorithm;

public class DifferAlgorithmConfig {
    public DiffAlgorithm diffAlgorithm;
    public boolean methods = true;
    public boolean instructions = true;

    public DifferAlgorithmConfig() {
    }

    public DifferAlgorithmConfig(DiffAlgorithm diffAlgorithm, boolean methods, boolean instructions) {
        this.diffAlgorithm = diffAlgorithm;
        this.methods = methods;
        this.instructions = instructions;
    }

    public DiffAlgorithm getDiffAlgorithm() {
        return diffAlgorithm;
    }

    public void setDiffAlgorithm(DiffAlgorithm diffAlgorithm) {
        this.diffAlgorithm = diffAlgorithm;
    }

    public boolean isMethods() {
        return methods;
    }

    public void setMethods(boolean methods) {
        this.methods = methods;
    }

    public boolean isInstructions() {
        return instructions;
    }

    public void setInstructions(boolean instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "DifferAlgorithmConfig{" +
                "differAlgorithm=" + diffAlgorithm +
                ", methods=" + methods +
                ", instructions=" + instructions +
                '}';
    }


}
