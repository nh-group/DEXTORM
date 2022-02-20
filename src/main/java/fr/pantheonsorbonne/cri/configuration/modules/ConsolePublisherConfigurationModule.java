package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.console.ConsoleRequirementsPublisher;

public class ConsolePublisherConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();
		this.bind(RequirementPublisher.class).to(ConsoleRequirementsPublisher.class);

	}

}
