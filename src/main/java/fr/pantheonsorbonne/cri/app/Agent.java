package fr.pantheonsorbonne.cri.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

import fr.pantheonsorbonne.cri.configuration.model.GeneralConfiguration;


import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.configuration.modules.InstrumentationConfigurationModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Agent {

    public static final String CONFIGURATION_FILE_KEY = "configurationFile";
    public static final String CONFIGURATION_FILE_SEPARATOR = ":";
    private static final Logger LOG = LoggerFactory.getLogger(Agent.class);

    @Inject
    Set<InstrumentationClient> instrumentatinClients;


    public static void premain(String agentArguments, Instrumentation instZ) throws IOException {

        // gather all modules to be used in the IC
        Optional<String> argProvidedConfigurationFile = getConfigFileFromParams(agentArguments);


        Collection<Module> loadedModules = new HashSet<>();
        loadedModules.add(new InstrumentationConfigurationModule(instZ));

        GeneralConfiguration appConfiguration = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

        String configurationFile = "dextorm.yaml";
        if (argProvidedConfigurationFile.isPresent()) {
            configurationFile = argProvidedConfigurationFile.get();
        }

        try (FileInputStream fis = new FileInputStream(configurationFile)) {
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

    protected static Optional<String> getConfigFileFromParams(String agentArguments) {
        if (agentArguments != null && !agentArguments.isBlank()) {
            Map<String, String> params = Arrays.stream(agentArguments.split("&"))
                    .filter(s -> s.split(CONFIGURATION_FILE_SEPARATOR).length == 2)
                    .map(s -> {
                                String[] kvParam = s.split(CONFIGURATION_FILE_SEPARATOR);
                                return Map.entry(kvParam[0], kvParam[1]);
                            }
                    )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            String configurationFilePath = params.get(CONFIGURATION_FILE_KEY);
            if (configurationFilePath != null) {
                File dextormConfigurationFile = Path.of(configurationFilePath).toAbsolutePath().toFile();
                LOG.info("using provided file for configuration: {}", dextormConfigurationFile);
                if (dextormConfigurationFile.exists()) {
                    return Optional.of(dextormConfigurationFile.getAbsolutePath());

                } else {
                    LOG.warn("file {} does not exist", dextormConfigurationFile);
                }
            }

        }

        return Optional.empty();
    }

    private void run() {

        for (InstrumentationClient ic : instrumentatinClients) {
            ic.registerClient();
        }

    }

}
