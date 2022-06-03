package fr.pantheonsorbonne.cri.reqmapping;

import java.util.*;
import java.util.stream.Collectors;

public class CompositeReqMatchImpl implements ReqMatch {
    final List<ReqMatch> requirementsMatches;

    public CompositeReqMatchImpl(ReqMatchImpl... matches) {
        this.requirementsMatches = Arrays.asList(matches);
    }

    @Override
    public boolean isMatch(StackTraceElement elt) {
        for (ReqMatch reqMatch : this.requirementsMatches) {
            if (reqMatch.isMatch(elt)) {
                //System.out.println(elt.toString() + " no match with" + reqMatch);
                return true;
            }
        }

        return false;

    }

    @Override
    public Collection<String> getRequirementsIds() {
        Set<String> res = new HashSet<>();
        for (ReqMatch m : this.requirementsMatches) {
            res.addAll(m.getRequirementsIds());
        }
        return res;

    }

    @Override
    public Collection<StackTraceElement> getMatchingTraceElement() {
        return this.requirementsMatches.stream()
                .map(r -> r.getMatchingTraceElement())
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

    }

    public <T extends ReqMatch> Optional<T> getComponent(Class<T> klass) {
        return (Optional<T>) this.requirementsMatches.stream().filter(m -> m.getClass().equals(klass)).findFirst();
    }

    @Override
    public String toString() {
        return "CompositeReqMatchImpl{" +
                "requirementsMatches=" + requirementsMatches.stream().map(r -> r.toString() + ";").collect(Collectors.joining()) +
                '}';
    }
}
