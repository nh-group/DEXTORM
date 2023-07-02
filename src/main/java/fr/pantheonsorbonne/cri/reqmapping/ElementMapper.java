package fr.pantheonsorbonne.cri.reqmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class ElementMapper {
    private final static Logger LOGGER = LoggerFactory.getLogger(ElementMapper.class);

    private final String instrumentedPackage;
    private final Map<String, List<StackTraceElement>> elements;

    private final Map<String, List<ReqMatch>> reqMatcherImpls;

    public ElementMapper(StackTraceElement[] elements, String instrumentedPackage, Set<ReqMatch> reqMatcherImpls) {
        this.elements = Arrays.stream(elements).collect(Collectors.groupingBy(e -> e.getFqClassName()));
        this.reqMatcherImpls = reqMatcherImpls.stream().collect(Collectors.groupingBy(r -> r.getFQClassName()));
        this.instrumentedPackage = instrumentedPackage;
    }

    public Set<String> getMatchingRequirementsIdSet() {

        return this.getMatchingRequirementsMatchers().stream().map(rm -> rm.getIssueIds()).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    public Set<ReqMatch> getMatchingRequirementsMatchers() {

        ExecutorService executorService = Executors.newFixedThreadPool(12);
        Set<ReqMatch> res = ConcurrentHashMap.newKeySet();
        for (String fqClassName : elements.keySet()) {
            if (this.reqMatcherImpls.containsKey(fqClassName)) {
                for (ReqMatch rm : this.reqMatcherImpls.get(fqClassName)) {
                    for (StackTraceElement stackTraceElement : elements.get(fqClassName)) {
                        executorService.submit(() -> {
                            if (rm.isMatch(stackTraceElement)) {
                                //System.out.println(elt + " ->" + m);
                                //LOGGER.trace("applying matcher {} ADDED", m);
                                res.add(rm);
                            }
                        });

                    }
                }
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return res;

    }


    public List<String> getMatchingRequirementsIdList() {

        return this.getMatchingRequirementsMatchers().stream().map(rm -> rm.getIssueIds()).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public Set<StackTraceElement> getMatchedElements() {
        return this.getMatchingRequirementsMatchers().stream().map(r -> r.getMatchingTraceElement()).flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
