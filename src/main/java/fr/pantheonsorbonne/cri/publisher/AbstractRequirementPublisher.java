package fr.pantheonsorbonne.cri.publisher;

import com.google.inject.Inject;
import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;

import java.util.Collection;

public abstract class AbstractRequirementPublisher implements RequirementPublisher {


    @Inject
    RequirementIssueDecorator requirementIssueDecorator;

    protected abstract void publishLinkedRequirement(Requirement req);

    @Override
    final public void publish(Requirement req) {
        this.publishLinkedRequirement(requirementIssueDecorator.getIssueLink(req));
    }

    @Override
    final public void publish(Collection<Requirement> reqToPublish) {
        for (Requirement r : reqToPublish) {
            this.publish(r);
        }

    }

}
