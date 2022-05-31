package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class ReqMatchImpl implements ReqMatch {

    protected final Set<String> commits = new HashSet<>();
    protected final String packageName;
    protected final String className;

    public ReqMatchImpl(String className, String packageName, String[] reqs) {
        this.className = className;
        this.packageName = packageName;
        commits.addAll(Arrays.asList(reqs));

    }

    public static ReqMatcherBuilder newBuilder() {
        return new ReqMatcherBuilder();
    }

    public String getClassName() {
        return this.className;
    }

    public Set<String> getReq() {
        return commits;
    }

    public void setReq(java.util.List<String> req) {
        this.commits.clear();
        this.commits.addAll(req);
    }

    protected boolean isMatchClass(StackTraceElement elt) {
        return this.getFQClassName().equals(elt.getPackageName() + "." + elt.getClassName().replaceAll("/", "."));
    }

    public String getFQClassName() {
        return this.packageName.isEmpty() ? className : this.packageName + "." + className;
    }

    @Override
    public String toString() {
        return "ReqMatch{" +
                "commits=" + commits +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
