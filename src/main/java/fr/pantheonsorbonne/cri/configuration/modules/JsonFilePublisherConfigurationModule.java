package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import fr.pantheonsorbonne.cri.configuration.model.publisher.JsonFilePublisherConfig;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;

public class JsonFilePublisherConfigurationModule extends AbstractModule {

    private final JsonFilePublisherConfig jsonPublisherConfig;

    public JsonFilePublisherConfigurationModule(JsonFilePublisherConfig jsonPublisherConfig) {
        this.jsonPublisherConfig = jsonPublisherConfig;
    }

    @Override
    protected void configure() {
        super.configure();

        this.bind(RequirementPublisher.class).to(JsonFilePublisher.class);
        this.bind(String.class).annotatedWith(Names.named("targetDir")).toInstance(this.jsonPublisherConfig.getTargetDir());


    }

}
