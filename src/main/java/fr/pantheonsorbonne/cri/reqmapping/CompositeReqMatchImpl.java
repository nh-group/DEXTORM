package fr.pantheonsorbonne.cri.reqmapping;

import java.util.*;

public class CompositeReqMatchImpl implements ReqMatch {
    final List<ReqMatch> matches;

    public CompositeReqMatchImpl(ReqMatchImpl... matches) {
        this.matches = Arrays.asList(matches);
    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        for (ReqMatch reqMatch : this.matches) {
            if (reqMatch.isMatch(elt)) {
                //System.out.println(elt.toString() + " no match with" + reqMatch);
                return true;
            }
        }

        return false;

    }

    @Override
    public Collection<String> getReq() {
        Collection<String> res = new LinkedList<>();
        for (ReqMatch m : this.matches) {
            res.addAll(m.getReq());
        }
        return res;

    }

    public <T extends ReqMatch> Optional<T> getComponent(Class<T> klass) {
        return (Optional<T>) this.matches.stream().filter(m -> m.getClass().equals(klass)).findFirst();
    }
}
