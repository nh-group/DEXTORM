package fr.pantheonsorbonne.cri.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import fr.pantheonsorbonne.cri.configuration.model.GeneralConfiguration;


import fr.pantheonsorbonne.cri.configuration.variables.DiffAlgorithm;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.configuration.modules.InstrumentationConfigurationModule;
import fr.pantheonsorbonne.cri.configuration.modules.RequirementMappingConfigurationModule;

public class Agent {

    @Inject
    Set<InstrumentationClient> instrumentatinClients;


    public static void premain(String arg, Instrumentation instZ) throws IOException {

        // gather all modules to be used in the IC




        Collection<Module> loadedModules = new HashSet<>();
        loadedModules.add(new InstrumentationConfigurationModule(instZ));

        GeneralConfiguration appConfiguration = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        try (FileInputStream fis = new FileInputStream("dextorm.yaml")) {
            appConfiguration = mapper.readValue(fis, GeneralConfiguration.class);
            appConfiguration.setInheritedModules(loadedModules);
            System.out.println(appConfiguration.toString());
        }

        // consolidate modules
        Module conf = Modules.combine(appConfiguration.getModules());

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
