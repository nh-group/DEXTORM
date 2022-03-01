package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;

import com.google.inject.name.Names;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.console.ConsoleRequirementsPublisher;

import javax.inject.Named;

public class ConsolePublisherConfigurationModule extends AbstractModule {
    private String fileName;

    public ConsolePublisherConfigurationModule(String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected void configure() {
        super.configure();
        this.bind(String.class).annotatedWith(Names.named("consolePublishers")).toInstance(this.fileName);
        this.bind(RequirementPublisher.class).to(ConsoleRequirementsPublisher.class);


    }

}
