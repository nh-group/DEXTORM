package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.List;
import java.util.function.Predicate;

public class LineReqMatch extends MethodReqMatch {
    private final Integer line;

    public LineReqMatch(String className, String packageName, String methodName, List<String> args, Integer line, String... reqs) {
        super(className, packageName, methodName, args, reqs);
        this.line = line;
    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        if (this.getFQClassName().equals(elt.getClassName().replaceAll("/", "."))) {
            if (elt.getLine() == this.getLine()) {
                return this.getReq().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
            }


        }
        return false;
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
