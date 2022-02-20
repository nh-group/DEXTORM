package fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import com.google.inject.name.Named;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Identified.Extendable;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;


public class MethodExtractorProvider implements javax.inject.Provider<Extendable> {

    @Inject
    @Named("instrumentedPackage")
    String instrumentedPackage;
    @Inject
    MethodCallInterceptor interceptor;

    @Provides
    @Singleton
    @Override
    public Extendable get() {

        return new AgentBuilder.Default().type(ElementMatchers.nameStartsWith(instrumentedPackage))
                .transform((Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
                            JavaModule module) -> builder.method(ElementMatchers.any())
                        .intercept(MethodDelegation.to(interceptor)));
    }

    ;

}
