package fr.pantheonsorbonne.cri.configuration;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.AbstractModule;

import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.ConfigurationFileParameters;

public class AppConfiguration extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();

		Path p = Paths.get("configuration.properties");
		if (Files.exists(p)) {
			try {
				Files.lines(p).forEachOrdered(System.out::println);
				bind(ApplicationParameters.class).to(ConfigurationFileParameters.class);
				return;
			} catch (IOException e) {
				
			}
		}
		
		System.out.println("Fallback on default configuration");
		bind(ApplicationParameters.class).to(DemoApplicationParameters.class);
		
		
		

	}

}
