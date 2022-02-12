package fr.pantheonsorbonne.cri.app;

import java.lang.instrument.Instrumentation;
import java.util.Set;

import fr.pantheonsorbonne.cri.publisher.console.configuration.ConsolePublisherConfiguration;
import org.eclipse.jgit.diff.DiffAlgorithm;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import fr.pantheonsorbonne.cri.configuration.AppConfiguration;
import fr.pantheonsorbonne.cri.configuration.GitRepoProvider;
import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.configuration.InstrumentationConfiguration;
import fr.pantheonsorbonne.cri.mapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.configuration.RequirementMappingConfiguration;
import fr.pantheonsorbonne.cri.mapping.impl.gumTree.GumTreeFileRequirementMappingProvider;
import fr.pantheonsorbonne.cri.publisher.grpc.configuration.GRPCPublisherConfiguration;

public class Agent {

	@Inject
	Set<InstrumentationClient> instrumentatinClients;

	public static void main(String args[]) {

		// gather all modules to be used in the IC
		Module applicationConfiguration = new AppConfiguration();

		// how do I match what's executed to a requirment?
		Module requirementMappingConfiguration = new RequirementMappingConfiguration(
				ApplicationParameters.DiffAlgorithm.GUMTREE);

		Module conf = Modules.combine(applicationConfiguration, requirementMappingConfiguration, new GitRepoProvider());

		Injector injector = Guice.createInjector(conf);

		RequirementMappingProvider provider = injector.getInstance(RequirementMappingProvider.class);
		provider.getReqMatcher().stream().forEach(System.out::println);

	}

	public static void premain(String arg, Instrumentation instZ) {

		// gather all modules to be used in the IC
		Module applicationConfiguration = new AppConfiguration();
		Injector configurationInjector = Guice.createInjector(applicationConfiguration);

		// how to I find what's executed?
		Module instrumentationConfiguration = new InstrumentationConfiguration(instZ);

		// how do I match what's executed to a requirment?
		Module requirementMappingConfiguration = new RequirementMappingConfiguration(
				fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters.DiffAlgorithm
						.valueOf(configurationInjector.getInstance(ApplicationParameters.class).getDiffAlgorithm())

		);
		Module gitRepoModule = new GitRepoProvider();

		// how do I tell the world?
		Module publisherConfiguration = new GRPCPublisherConfiguration();
		//Module publisherConfiguration = new ConsolePublisherConfiguration();


		// consolidate modules
		Module conf = Modules.combine(applicationConfiguration, requirementMappingConfiguration,
				instrumentationConfiguration, publisherConfiguration, gitRepoModule);

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
