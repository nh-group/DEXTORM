package fr.pantheonsorbonne.cri.reqmapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CompositeReqMatchImpl implements ReqMatch {
    final List<ReqMatch> matches;

    public CompositeReqMatchImpl(ReqMatchImpl... matches) {
        this.matches = Arrays.asList(matches);
    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        for (ReqMatch reqMatch : this.matches) {
            if (!reqMatch.isMatch(elt)) {
                return false;
            }
        }

        return true;

    }

    @Override
    public Collection<String> getReq() {
        Collection<String> res = new LinkedList<>();
        for (ReqMatch m : this.matches) {
            res.addAll(m.getReq());
        }
        return res;

    }
}
