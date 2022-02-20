package fr.pantheonsorbonne.cri.publisher;

import java.util.Collection;

import fr.pantheonsorbonne.cri.model.requirements.Requirement;

public interface RequirementPublisher {
	
	

	void publish(Requirement req);

	void publish(Collection<Requirement> reqToPublish);

}
