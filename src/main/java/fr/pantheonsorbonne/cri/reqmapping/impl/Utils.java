package fr.pantheonsorbonne.cri.reqmapping.impl;

import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor.CantComputeParameterException;

public class Utils {

    public static String typeToCodeJVM(String type) throws CantComputeParameterException {
        switch (type) {
            case "boolean":
                return "Z";
            case "byte":
                return "B";
            case "char":
                return "C";
            case "short":
                return "S";
            case "int":
                return "I";
            case "long":
                return "J";
            case "float":
                return "F";
            case "double":
                return "D";
            case "void":
                return "V";
            case "final":
                return "";
            case "":
                return "";
        }
        throw new CantComputeParameterException("failed to convert type " + type);
    }
}
