package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;

import java.lang.instrument.Instrumentation;

public class InstrumentationConfigurationModule extends AbstractModule {
    private final Instrumentation instrumentation;

    public InstrumentationConfigurationModule(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public InstrumentationConfigurationModule() {
        this.instrumentation = null;
    }

    @Override
    protected void configure() {

        super.configure();
        if (this.instrumentation == null) {
            install(new JacocoConfigurationModule());
        } else {
            install(new BBudyConfigurationModule(this.instrumentation));
        }


    }

}
