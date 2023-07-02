package fr.pantheonsorbonne.cri.reqmapping;

import java.util.*;

public abstract class ReqMatchImpl implements ReqMatch {

    protected final Set<String> issueIds = new HashSet<>();

    @Override
    public Set<String> getCommitIds() {
        return commitIds;
    }

    protected final Set<String> commitIds = new HashSet<>();
    protected final Set<StackTraceElement> matches = new HashSet<>();
    protected final String packageName;
    protected final String className;
    protected final String fqClassName;

    public ReqMatchImpl(String className, String packageName, String[] issueIds, String[] commitIds) {
        this.className = className;
        this.packageName = packageName;
        this.issueIds.addAll(List.of(issueIds));
        this.commitIds.addAll(Arrays.asList(commitIds));
        if (this.className != null && !this.className.isEmpty()) {
            this.fqClassName = this.packageName.isEmpty() ? className : this.packageName + "." + className;
        } else {
            this.fqClassName = "";
        }

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

    public Set<String> getIssueIds() {
        return issueIds;
    }

    public Collection<StackTraceElement> getMatchingTraceElement() {
        return this.matches;
    }

    @Override
    public String getFQClassName() {
        return this.fqClassName;
    }

    public void setIssueIds(java.util.List<String> issueIds) {
        this.issueIds.clear();
        this.issueIds.addAll(issueIds);
    }

    public String getClassName() {
        return this.className;
    }

    protected boolean isMatchFQClass(StackTraceElement elt) {
        return this.getFQClassName().equals(elt.getFqClassName());
    }

    @Override
    public String toString() {
        return "ReqMatchImpl{" +
                "issueIds=" + issueIds +
                ", commitIds=" + commitIds +
                ", matches=" + matches +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", fqClassName='" + fqClassName + '\'' +
                '}';
    }
}
