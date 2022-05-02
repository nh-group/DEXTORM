package fr.pantheonsorbonne.cri.reqmapping;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MethodReqMatch extends ReqMatch {
    private final List<String> args = new ArrayList<>();
    private final String methodName;

    public MethodReqMatch(String className, String packageName, String methodName, List<String> args, String... reqs) {
        super(className, packageName, reqs);
        this.methodName = methodName;
        this.args.addAll(args);
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        return this.getFQClassName().equals(elt.getClassName().replaceAll("/", "."))
                && this.getMethodName().equals(elt.getMethodName().split("\\$")[0])

                //how do we take method args into account in this one?
                && this.getReq().stream().anyMatch(Predicate.not(Strings::isNullOrEmpty));
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return "MethodReqMatch{" +
                "args=" + args +
                ", methodName='" + methodName + '\'' + super.toString() +
                '}';
    }
}
