package fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy.configuration;

import java.lang.instrument.Instrumentation;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;

import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy.ByteBuddyInstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy.MethodExtractorProvider;
import net.bytebuddy.agent.builder.AgentBuilder.Identified.Extendable;

public class BBudyConfiguration extends AbstractModule {

	private Instrumentation instrumentation;

	public BBudyConfiguration(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
	}

	@Override
	protected void configure() {
		super.configure();

		requestStaticInjection(ByteBuddyInstrumentationClient.class);

		Multibinder<InstrumentationClient> multibinder = Multibinder.newSetBinder(binder(),
				InstrumentationClient.class);
		multibinder.addBinding().to(ByteBuddyInstrumentationClient.class);

		Multibinder<Extendable> multibinderExtendable = Multibinder.newSetBinder(binder(), Extendable.class);
		multibinderExtendable.addBinding().toProvider(MethodExtractorProvider.class);

	}

	@Provides
	public Instrumentation getInstrumentation() {
		return this.instrumentation;
	}

}
