package fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.StackTraceParser;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class MethodCallInterceptor {

	@Inject
	public RequirementPublisher publisher;
	@Inject
	RequirementMappingProvider mapper;
	@Inject
	@Named("instrumentedPackage")
	String instrumentedPackage;

	@RuntimeType
	public Object intercept(@SuperCall Callable<?> zuper, @AllArguments Object... args) throws Exception {
		Collection<String> reqs = new StackTraceParser(Thread.getAllStackTraces().get(Thread.currentThread()),instrumentedPackage,
				mapper.getReqMatcher()).getReqs();
		if (!reqs.isEmpty()) {
			reqs.stream().collect(Collectors.toSet()).stream()
					.map((String req) -> Requirement.newBuilder().setId(req).build())
					.forEach((Requirement req) -> publisher.publish(req));

		}
		return zuper.call();
	}
}