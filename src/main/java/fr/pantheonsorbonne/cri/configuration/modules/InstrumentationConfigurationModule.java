package fr.pantheonsorbonne.cri.configuration.modules;

import java.lang.instrument.Instrumentation;

import com.google.inject.AbstractModule;

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
