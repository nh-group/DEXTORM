package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LineReqMatchImpl extends ReqMatchImpl implements Comparable<LineReqMatchImpl> {
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
        return "LineReqMatchImpl" + " " + this.packageName + "." + this.className + "(" + this.line + ") req=" + this.getRequirementsIds().stream().collect(Collectors.joining(",")) + ") :" + this.commits.stream().collect(Collectors.joining(","));
    }

    @Override
    public int compareTo(LineReqMatchImpl lineReqMatch) {
        int p = this.packageName.compareTo(lineReqMatch.packageName);
        int c = this.className.compareTo(lineReqMatch.className);
        int l = this.line.compareTo(lineReqMatch.line);
        if (p == 0) {
            if (c == 0) {
                return l;
            } else {
                return c;
            }
        } else {
            return p;
        }
    }
}
