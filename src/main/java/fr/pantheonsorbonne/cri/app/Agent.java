package fr.pantheonsorbonne.cri.app;

import java.lang.instrument.Instrumentation;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import fr.pantheonsorbonne.cri.configuration.AppConfiguration;
import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.configuration.InstrumentationConfiguration;
import fr.pantheonsorbonne.cri.mapping.configuration.RequirementMappingConfiguration;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.SimpleGitFileVisitor;
import fr.pantheonsorbonne.cri.publisher.grpc.configuration.GRPCPublisherConfiguration;

public class Agent {

	@Inject
	Set<InstrumentationClient> instrumentatinClients;

	public static void main(String args[]) {

		// gather all modules to be used in the IC
		Module applicationConfiguraiton = new AppConfiguration();

		// how do I match what's executed to a requirment?
		Module requirementMappingConfiguration = new RequirementMappingConfiguration();

		Module conf = Modules.combine(applicationConfiguraiton, requirementMappingConfiguration);

		Injector injector = Guice.createInjector(conf);
		
		SimpleGitFileVisitor visitor = new SimpleGitFileVisitor(new DemoApplicationParameters());
		
		
	

	}

	public static void premain(String arg, Instrumentation instZ) {

		// gather all modules to be used in the IC
		Module applicationConfiguraiton = new AppConfiguration();

		// how to I find what's executed?
		Module instrumentationConfiguration = new InstrumentationConfiguration(instZ);

		// how do I match what's executed to a requirment?
		Module requirementMappingConfiguration = new RequirementMappingConfiguration();

		// how do I tell the world?
		Module publisherConfiguration = new GRPCPublisherConfiguration();

		// consolidate modules
		Module conf = Modules.combine(applicationConfiguraiton, requirementMappingConfiguration,
				instrumentationConfiguration, publisherConfiguration);

		// create the agent a hook instrumentation directives
		Injector injector = Guice.createInjector(conf);
		Agent agent = injector.getInstance(Agent.class);
		agent.run();

	}

	private void run() {

		for (InstrumentationClient ic : instrumentatinClients) {
			ic.registerClient();
		}

	}

}
