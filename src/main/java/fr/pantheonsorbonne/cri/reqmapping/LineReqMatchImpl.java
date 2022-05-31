package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.function.Predicate;

public class LineReqMatchImpl extends ReqMatchImpl {
    private final Integer line;

    public LineReqMatchImpl(String className, String packageName, Integer line, String[] reqs) {
        super(className, packageName, reqs);
        this.line = line;
    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        return super.isMatchClass(elt)
                && (elt.getLine() == this.getLine())
                && this.getReq().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
    }

    public Integer getLine() {
        return line;
    }


    @Override
    public String toString() {
        return "LineReqMatch{" +
                "line=" + line + super.toString() +
                '}';
    }
}
