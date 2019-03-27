package fr.pantheonsorbonne.cri.instrumentation.configuration;

import java.lang.instrument.Instrumentation;

import com.google.inject.AbstractModule;
import fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy.configuration.BBudyConfiguration;

public class InstrumentationConfiguration extends AbstractModule {
	private Instrumentation instrumentation;

	public InstrumentationConfiguration(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
	}

	@Override
	protected void configure() {

		super.configure();

		install(new BBudyConfiguration(this.instrumentation));

	}

}
