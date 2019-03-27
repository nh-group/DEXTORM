package fr.pantheonsorbonne.cri.configuration;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;

public class AppConfiguration extends AbstractModule {

	

	@Override
	protected void configure() {
		super.configure();

		bind(ApplicationParameters.class).to(DemoApplicationParameters.class);
		
	

	}

	

}
