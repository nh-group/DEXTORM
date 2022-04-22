package fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy;

import com.google.inject.Inject;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import net.bytebuddy.agent.builder.AgentBuilder.Identified.Extendable;

import java.lang.instrument.Instrumentation;
import java.util.Set;

public class ByteBuddyInstrumentationClient implements InstrumentationClient {

    @Inject
    Set<Extendable> extendables;

    @Inject
    Instrumentation inst;

    @Override
    public void registerClient() {
        for (Extendable ext : extendables) {
            ext.installOn(inst);
        }

    }

}
