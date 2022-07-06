package fr.pantheonsorbonne.cri.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import fr.pantheonsorbonne.cri.configuration.model.GeneralConfiguration;
import fr.pantheonsorbonne.cri.configuration.modules.InstrumentationConfigurationModule;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;

public class DextormBench {


    @Benchmark
    @Fork(value = 1, warmups = 0)
    @Warmup(iterations = 0)
    @Measurement(iterations = 1)
    @BenchmarkMode(Mode.SingleShotTime)
    public void benchDextorm(ExecutionPlan executionPlan) throws IOException, URISyntaxException {
        // gather all modules to be used in the IC


        String argProvidedConfigurationFile = new File(getClass().getClassLoader().getResource("benchmark/" + executionPlan.project + "/" + executionPlan.diffAlgo + "/" + executionPlan.scope + ".yaml").toURI()).getAbsolutePath();
        String coverageReport = new File(getClass().getClassLoader().getResource("benchmark/" + executionPlan.project + ".xml").toURI()).getAbsolutePath();


        Collection<Module> loadedModules = new HashSet<>();
        loadedModules.add(new InstrumentationConfigurationModule());
        loadedModules.add(new AbstractModule() {
            @Override
            protected void configure() {
                super.bind(String.class).annotatedWith(Names.named("jacocoReport")).toInstance(coverageReport);
            }
        });

        GeneralConfiguration appConfiguration = null;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);


        try (FileInputStream fis = new FileInputStream(argProvidedConfigurationFile)) {
            appConfiguration = mapper.readValue(fis, GeneralConfiguration.class);
            appConfiguration.setInheritedModules(loadedModules);

        }


        // consolidate modules
        Module conf = Modules.combine(appConfiguration.getModules());


        // create the agent a hook instrumentation directives
        Injector injector = Guice.createInjector(conf);
        Agent agent = injector.getInstance(Agent.class);
        agent.run();
    }
}