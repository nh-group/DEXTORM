package fr.pantheonsorbonne.cri.publisher;

import fr.pantheonsorbonne.cri.model.requirements.Requirement;

import java.util.Collection;

public interface RequirementPublisher {

    void publishNow(Requirement req);

    void publishNow(Collection<Requirement> reqToPublish);

    void publishNow(String project, String issue, String method, COVERAGE_TYPE coverageType, double coverage, int count);

    void collect(String project, String issue, String method, COVERAGE_TYPE coverageType, double coverage, int count);

    void flush();

    enum COVERAGE_TYPE {
        METHODS,
        LINES
    }


}
