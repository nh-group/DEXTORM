package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MethodReqMatchImpl extends ReqMatchImpl {
    private final List<String> args = new ArrayList<>();
    private final String methodName;

    public MethodReqMatchImpl(String className, String packageName, String methodName, List<String> args, String... issueIds) {
        super(className, packageName, issueIds);
        this.methodName = methodName;
        this.args.addAll(args);
        //System.out.println(this);
    }

    @Override
    protected boolean isMatchLogged(StackTraceElement elt) {
        return super.isMatchFQClass(elt)
                && this.getMethodName().equals(elt.getMethodName().split("\\$")[0])
                && this.getArgs().equals(elt.getMethodArgs())
                && this.getRequirementsIds().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "MethodReqMatchImpl" + " " + this.packageName + "." + this.className + "." + this.methodName + "(" + this.args.stream().collect(Collectors.joining(",")) + ") :" + this.issueIds.stream().collect(Collectors.joining(","));
    }
}
