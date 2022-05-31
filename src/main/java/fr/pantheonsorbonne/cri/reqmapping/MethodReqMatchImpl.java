package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MethodReqMatchImpl extends ReqMatchImpl {
    private final List<String> args = new ArrayList<>();
    private final String methodName;

    public MethodReqMatchImpl(String className, String packageName, String methodName, List<String> args, String... reqs) {
        super(className, packageName, reqs);
        this.methodName = methodName;
        this.args.addAll(args);
        //System.out.println(this);
    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        return super.isMatchClass(elt)
                && this.getMethodName().equals(elt.getMethodName().split("\\$")[0])
                && Arrays.equals(this.getArgs().toArray(), ReqMatcherBuilder.strArgsToList(elt.getMethodArgs()).toArray())
                && this.getReq().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return this.packageName + "." + this.className + "." + this.methodName + "(" + this.args.stream().collect(Collectors.joining(",")) + ") :" + this.commits.stream().collect(Collectors.joining(","));
    }
}
