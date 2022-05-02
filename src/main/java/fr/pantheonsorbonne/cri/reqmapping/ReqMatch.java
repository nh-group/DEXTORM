package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class ReqMatch {

    protected final Set<String> commits = new HashSet<>();
    protected final String packageName;
    protected final String className;

    public ReqMatch(String className, String packageName, String... reqs) {
        this.className = className;
        this.packageName = packageName;
        commits.addAll(Arrays.asList(reqs));

    }

    public static ReqMatcherBuilder newBuilder() {
        return new ReqMatcherBuilder();
    }


    public String getFQClassName() {
        return this.packageName.isEmpty() ? className : this.packageName + "." + className;
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

    public abstract boolean isMatch(StackTraceElement elt);

    @Override
    public String toString() {
        return "ReqMatch{" +
                "commits=" + commits +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
