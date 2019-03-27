package fr.pantheonsorbonne.cri.publisher;

import java.util.Collection;

import com.google.inject.Inject;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.requirements.Requirement;

public abstract class AbstractRequirementPublisher implements RequirementPublisher {

	@Inject
	ApplicationParameters vars;

	protected abstract void publishLinkedRequirement(Requirement req);

	@Override
	final public void publish(Requirement req) {
		this.publishLinkedRequirement(vars.getRequirementIssueDecorator().getIssueLink(req));
	}

	@Override
	final public void publish(Collection<Requirement> reqToPublish) {
		for (Requirement r : reqToPublish) {
			this.publish(r);
		}

	}

}
