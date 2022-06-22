package fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.reqmapping.ElementMapper;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.StackTraceElement;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.Identified.Extendable;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;


public class MethodExtractorProvider implements javax.inject.Provider<Extendable> {

    @Inject
    @Named("instrumentedPackage")
    String instrumentedPackage;
    @Inject
    MethodCallInterceptor interceptor;

    //TODO: use those from jacoco
    @Inject
    @Named("DoMethodsDiff")
    Boolean doMethods;
    @Inject
    @Named("DoInstructionsDiff")
    Boolean doInstructions;

    @Provides
    @Singleton
    @Override
    public Extendable get() {

        return new AgentBuilder.Default().type(ElementMatchers.nameStartsWith(instrumentedPackage))
                .transform((Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader,
                            JavaModule module) -> builder.method(ElementMatchers.any())
                        .intercept(MethodDelegation.to(interceptor)));
    }

    public static class MethodCallInterceptor {

        @Inject
        public RequirementPublisher publisher;
        @Inject
        RequirementMappingProvider mapper;
        @Inject
        @Named("instrumentedPackage")
        String instrumentedPackage;

        @RuntimeType
        public Object intercept(@SuperCall Callable<?> zuper, @AllArguments Object... args) throws Exception {
            //I don't know how to get the params from the stacktrace, but it's not used at the moment I'll put a deadly exception to remind me of this
            System.out.println("check the problem in the code and read the comment");
            System.exit(-1);
            List<StackTraceElement> ste = Arrays.stream(Thread.getAllStackTraces().get(Thread.currentThread())).map(e -> new StackTraceElement(e.getFileName(), instrumentedPackage
                    , e.getClassName(), e.getMethodName(), "", e.getLineNumber())).collect(Collectors.toList());

            Collection<String> reqs = new ElementMapper(ste.toArray(new StackTraceElement[0]), instrumentedPackage,
                    mapper.getReqMatcher()).getMatchingRequirementsIdSet();

            reqs.stream().collect(Collectors.toSet()).stream()
                    .map((String req) -> Requirement.newBuilder().setId(req).build())
                    .forEach((Requirement req) -> publisher.publishNow(req));

            return zuper.call();
        }
    }
}
