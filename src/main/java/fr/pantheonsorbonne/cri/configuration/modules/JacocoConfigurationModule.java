package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy.ByteBuddyInstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy.MethodExtractorProvider;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.JacocoInstrumentationClient;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

public class JacocoConfigurationModule extends AbstractModule {


    @Override
    protected void configure() {
        super.configure();

        Multibinder<InstrumentationClient> multibinder = Multibinder.newSetBinder(binder(),
                InstrumentationClient.class);
        multibinder.addBinding().to(JacocoInstrumentationClient.class);


    }


}
