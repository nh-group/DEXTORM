package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LineReqMatchImpl extends ReqMatchImpl {
    private final Integer line;

    public LineReqMatchImpl(String className, String packageName, Integer line, String[] reqs) {
        super(className, packageName, reqs);
        this.line = line;
    }

    @Override
    protected boolean isMatchLogged(StackTraceElement elt) {
        return super.isMatchFQClass(elt)
                && (elt.getLine() == this.getLine())
                && this.getRequirementsIds().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
    }

    public Integer getLine() {
        return line;
    }


    @Override
    public String toString() {
        return this.packageName + "." + this.className + "." + this.line + "(" + this.getRequirementsIds().stream().collect(Collectors.joining(",")) + ") :" + this.commits.stream().collect(Collectors.joining(","));
    }
}
