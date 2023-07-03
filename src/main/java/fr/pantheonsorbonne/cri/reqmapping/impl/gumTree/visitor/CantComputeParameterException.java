package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor;

public class CantComputeParameterException extends Exception{
    public CantComputeParameterException(String s) {
        super(s);
    }

    public CantComputeParameterException(RuntimeException e) {
        super(e);
    }
}
