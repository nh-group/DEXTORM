package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReqMatcherBuilder implements Cloneable {

    private final List<String> args = new ArrayList<>();
    private final List<String> reqs = new ArrayList<>();
    private String packageName = null;
    private String methodName = null;
    private String className = null;
    private Integer line;

    public ReqMatcherBuilder arg(String arg) {
        this.args.add(arg);
        return this;
    }

    public ReqMatcherBuilder commit(String commit) {
        if (!Strings.isNullOrEmpty(commit)) {
            this.reqs.add(commit);
        }

        return this;
    }

    public ReqMatch build() {

        if (line == null) {
            return new MethodReqMatch(this.className, this.packageName, this.methodName, this.args, this.reqs.toArray(new String[0]));
        } else {
            return new LineReqMatch(this.className, this.packageName, this.methodName, this.args, this.line, this.reqs.toArray(new String[0]));
        }

    }


    public ReqMatcherBuilder commits(Collection<String> commits) {

        this.reqs.addAll(
                commits.stream().filter(Predicate.not(Strings::isNullOrEmpty)).collect(Collectors.toList()));
        return this;
    }

    public ReqMatcherBuilder args(List<String> args) {
        this.args.addAll(args);
        return this;
    }

    public ReqMatcherBuilder packageName(String label) {
        this.packageName = label;
        return this;

    }

    public ReqMatcherBuilder methodName(String name) {
        this.methodName = name;
        return this;
    }

    public ReqMatcherBuilder className(String name) {
        this.className = name;
        return this;
    }

    public ReqMatcherBuilder line(int lineNumber) {
        this.line = lineNumber;
        return this;
    }

    public ReqMatcherBuilder getCopy() {
        ReqMatcherBuilder res = new ReqMatcherBuilder();
        res.args.addAll(this.args);
        res.reqs.addAll(this.reqs);
        res.className = this.className;
        res.line = this.line;
        res.methodName = this.methodName;
        res.packageName = this.packageName;
        res.reqs.addAll(this.reqs);
        return res;
    }
}
