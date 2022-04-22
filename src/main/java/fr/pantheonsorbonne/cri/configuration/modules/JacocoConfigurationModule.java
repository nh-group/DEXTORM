package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.JacocoInstrumentationClient;

public class JacocoConfigurationModule extends AbstractModule {


    @Override
    protected void configure() {
        super.configure();

        Multibinder<InstrumentationClient> multibinder = Multibinder.newSetBinder(binder(),
                InstrumentationClient.class);
        multibinder.addBinding().to(JacocoInstrumentationClient.class);


    }


}
