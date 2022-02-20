package fr.pantheonsorbonne.cri.configuration.modules;

import java.lang.instrument.Instrumentation;

import com.google.inject.AbstractModule;

public class InstrumentationConfigurationModule extends AbstractModule {
	private Instrumentation instrumentation;

	public InstrumentationConfigurationModule(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
	}

	@Override
	protected void configure() {

		super.configure();

		install(new BBudyConfigurationModule(this.instrumentation));

	}

}
