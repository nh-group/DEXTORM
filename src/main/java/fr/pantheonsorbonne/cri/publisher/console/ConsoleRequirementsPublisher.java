package fr.pantheonsorbonne.cri.publisher.console;

import java.util.Collection;
import java.util.logging.Level;


import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleRequirementsPublisher implements RequirementPublisher {

    private static final Logger logger = LoggerFactory.getLogger("ConsoleRequirementsPublisher");

    @Override
    public void publish(Requirement req) {
        logger.info(req.getId());
        

    }

    @Override
    public void publish(Collection<Requirement> reqToPublish) {
        for (Requirement r : reqToPublish) {
            this.publish(r);
        }

    }

}
