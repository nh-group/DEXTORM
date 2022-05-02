package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.rest.APIRequirementPublisher;

public class APIPublisherConfigurationModule extends AbstractModule {
    String baseUrl;

    public APIPublisherConfigurationModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    protected void configure() {
        super.configure();

        this.bind(RequirementPublisher.class).to(APIRequirementPublisher.class);
        this.bind(String.class).annotatedWith(Names.named("baseUrl")).toInstance(baseUrl);


    }
}
