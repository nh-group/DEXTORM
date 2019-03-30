package fr.pantheonsorbonne.cri.configuration;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.ConfigurationFileParameters;

public class AppConfiguration extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		if (Files.exists(Paths.get("configuration.properties"))) {
			bind(ApplicationParameters.class).to(ConfigurationFileParameters.class);
		} else {
			System.out.println("Fallback on default configuration");
			bind(ApplicationParameters.class).to(DemoApplicationParameters.class);
		}
		
		

	}

}
