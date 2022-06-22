package fr.pantheonsorbonne.cri.publisher;

import fr.pantheonsorbonne.cri.model.requirements.Requirement;

import java.util.Collection;

public interface RequirementPublisher {


    void publishNow(Requirement req);

    void publishNow(Collection<Requirement> reqToPublish);

    void publishNow(String project, String issue, String method, double lineCoverage, double methodCoverage, int countLine, int countMethod);

    void collect(String project, String issue, String method, double lineCoverage, double methodCoverage, int countLine, int countMethod);

    void flush();


}
