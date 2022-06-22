package fr.pantheonsorbonne.cri.publisher.rest;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.AbstractRequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.rest.model.Gumtree;
import fr.pantheonsorbonne.cri.publisher.rest.model.IssueReport;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class APIRequirementPublisher extends AbstractRequirementPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(APIRequirementPublisher.class);
    final Client client;
    final WebTarget target;
    @Named("gitHubRepoName")
    @Inject
    String repoName;

    Map<String, Object> payload = new HashMap<>();

    @Inject
    public APIRequirementPublisher(@Named("baseUrl") String baseUrl) {
        this.client = JerseyClientBuilder.createClient();
        this.target = this.client.target(baseUrl);

    }

    @Override
    protected void publishLinkedRequirement(Requirement req) {

        try {


            Object payload = (Map.of(repoName, (Map.of(Integer.valueOf(req.getId()), new IssueReport(new Gumtree(1, 2))))));

            Response resp = this.target.request().post(Entity.json(payload));
            if (!resp.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                LOG.error("{} {}", resp.getStatusInfo().getStatusCode(), resp.getStatusInfo().getReasonPhrase());
            }
        } catch (ProcessingException pe) {
            LOG.error(pe.getMessage());
        }

    }

    @Override
    public void publishNow(String project, String issue, String method, double lineCoverage, double methodCoverage, int countLine, int countMethod) {
        Object payload =
                (Map.of(repoName,
                        Map.of("timestamp", System.currentTimeMillis(), "issues",
                                (Map.of(issue, Map.of("gumtree", Map.of("lineCoverage", lineCoverage * 100, "methodCoverage", methodCoverage * 100)))))));


        Response resp = this.target.request().post(Entity.json(payload));
        if (!resp.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            LOG.error("{} {}", resp.getStatusInfo().getStatusCode(), resp.getStatusInfo().getReasonPhrase());
        }
    }

    @Override
    public void collect(String project, String issue, String method, double lineCoverage, double methodCoverage, int countLine, int countMethod) {
        payload.put(issue, Map.of(method, Map.of("lineCoverage", lineCoverage * 100, "methodCoverage", methodCoverage * 100)));
    }

    @Override
    public void flush() {
        Object payload = Map.of("issues", this.payload);
        Response resp = this.target.request().post(Entity.json(payload));
        if (!resp.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            LOG.error("{} {}", resp.getStatusInfo().getStatusCode(), resp.getStatusInfo().getReasonPhrase());
        }
    }
}
