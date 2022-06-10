package fr.pantheonsorbonne.cri.publisher;

import fr.pantheonsorbonne.cri.model.requirements.Requirement;

import java.util.Collection;

public interface RequirementPublisher {


    void publish(Requirement req);

    void publish(Collection<Requirement> reqToPublish);

    void publish(String project, String issue, String method, double lineCoverage, double methodCoverage, int countLine, int countMethod);

}
