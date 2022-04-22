package fr.pantheonsorbonne.cri.reqmapping;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ReqMatch {

    private final ArrayList<String> commits = new ArrayList();
    private final String packageName;
    private final String className;

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


    public java.util.List<String> getReq() {
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
