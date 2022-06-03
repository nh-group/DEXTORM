package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ReqMatchImpl implements ReqMatch {

    protected final Set<String> commits = new HashSet<>();
    protected final Set<StackTraceElement> matches = new HashSet<>();
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

    public final boolean isMatch(StackTraceElement elt) {
        boolean isAMatch = isMatchLogged(elt);
        if (isAMatch) {
            this.matches.add(elt);
        }
        return isAMatch;
    }

    protected abstract boolean isMatchLogged(StackTraceElement elt);

    public Set<String> getRequirementsIds() {
        return commits;
    }

    public Collection<StackTraceElement> getMatchingTraceElement() {
        return this.matches;
    }

    public void setReq(java.util.List<String> req) {
        this.commits.clear();
        this.commits.addAll(req);
    }

    public String getClassName() {
        return this.className;
    }

    protected boolean isMatchFQClass(StackTraceElement elt) {
        return this.getFQClassName().equals(elt.getFQClassName());
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
