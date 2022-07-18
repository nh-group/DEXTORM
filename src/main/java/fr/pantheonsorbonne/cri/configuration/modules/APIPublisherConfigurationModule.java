package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class APIPublisherConfigurationModule extends AbstractModule {
    String baseUrl;

    public APIPublisherConfigurationModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    protected void configure() {
        super.configure();
        //there was a class APIRequirementPublisher but it wasn't used, so I removed it
        //this.bind(RequirementPublisher.class).to(APIRequirementPublisher.class);
        this.bind(String.class).annotatedWith(Names.named("baseUrl")).toInstance(baseUrl);


    }
}
