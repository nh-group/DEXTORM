package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class LineReqMatch extends ReqMatch {
    private final ArrayList<String> args = new ArrayList<>();
    private final Integer line;


    public LineReqMatch(String className, String packageName, Integer line, String... reqs) {
        super(className, packageName, reqs);
        this.line = line;

    }

    public Collection<String> getArgs() {
        return args;
    }

    public void setArgs(java.util.List<String> args) {
        this.args.clear();
        this.args.addAll(args);

    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        return this.getFQClassName().equals(elt.getClassName().replaceAll("/", "."))
                && elt.getLineNumber() == this.getLine()
                && this.getReq().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
    }

    public Integer getLine() {
        return line;
    }
}
