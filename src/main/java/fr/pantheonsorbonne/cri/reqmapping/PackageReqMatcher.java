package fr.pantheonsorbonne.cri.reqmapping;

import org.jetbrains.annotations.NotNull;

public class PackageReqMatcher extends ReqMatchImpl implements Comparable<PackageReqMatcher>{
    public PackageReqMatcher(String className, String packageName, String[] issueId, String[] reqs) {
        super(className, packageName, issueId, reqs);
    }

    @Override
    protected boolean isMatchLogged(StackTraceElement elt) {
        return this.packageName.equals(elt.getPackageName());
    }

    @Override
    public int compareTo(@NotNull PackageReqMatcher packageReqMatcher) {
        return this.packageName.compareTo(packageReqMatcher.packageName);
    }
}
