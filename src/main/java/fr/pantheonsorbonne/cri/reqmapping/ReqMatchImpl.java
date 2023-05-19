package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ReqMatchImpl implements ReqMatch {

    protected final Set<String> issueIds = new HashSet<>();
    protected final Set<StackTraceElement> matches = new HashSet<>();
    protected final String packageName;
    protected final String className;
    protected final String fqClassName;

    public ReqMatchImpl(String className, String packageName, String[] issueIds) {
        this.className = className;
        this.packageName = packageName;
        this.issueIds.addAll(Arrays.asList(issueIds));
        if(this.className!=null && !this.className.isEmpty()) {
            this.fqClassName = this.packageName.isEmpty() ? className : this.packageName + "." + className;
        }
        else{
            this.fqClassName="";
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

    public Set<String> getRequirementsIds() {
        return issueIds;
    }

    public Collection<StackTraceElement> getMatchingTraceElement() {
        return this.matches;
    }

    @Override
    public String getFQClassName() {
        return this.fqClassName;
    }

    public void setReq(java.util.List<String> req) {
        this.issueIds.clear();
        this.issueIds.addAll(req);
    }

    public String getClassName() {
        return this.className;
    }

    protected boolean isMatchFQClass(StackTraceElement elt) {
        return this.getFQClassName().equals(elt.getFqClassName());
    }

    @Override
    public String toString() {
        return "ReqMatch{" +
                "commits=" + issueIds +
                ", packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
