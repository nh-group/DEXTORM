package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.AbstractRequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class APIRequirementPublisher extends AbstractRequirementPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(APIRequirementPublisher.class);
    final Client client;
    final WebTarget target;

    @Inject
    public APIRequirementPublisher(@Named("baseUrl") String baseUrl) {
        this.client = JerseyClientBuilder.createClient();
        this.target = this.client.target(baseUrl);

    }

    @Override
    protected void publishLinkedRequirement(Requirement req) {

        try {
            Response resp = this.target.request().post(Entity.json(req));
            if (!resp.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                LOG.error("{} {}", resp.getStatusInfo().getStatusCode(), resp.getStatusInfo().getReasonPhrase());
            }
        }
        catch(ProcessingException pe){
            LOG.error(pe.getMessage());
        }

    }
}
