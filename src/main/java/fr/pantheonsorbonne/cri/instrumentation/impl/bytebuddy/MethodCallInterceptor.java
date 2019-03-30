package fr.pantheonsorbonne.cri.instrumentation.impl.bytebuddy;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.google.inject.Inject;

import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.mapping.RepoRequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.StackTraceParser;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.requirements.Requirement;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class MethodCallInterceptor {

	@Inject
	public RequirementPublisher publisher;

	@Inject
	DemoApplicationParameters vars;

	@Inject
	RepoRequirementMappingProvider mapper;

	@RuntimeType
	public Object intercept(@SuperCall Callable<?> zuper, @AllArguments Object... args) throws Exception {
		Collection<String> reqs = new StackTraceParser(Thread.getAllStackTraces().get(Thread.currentThread()), vars,
				mapper.getReqMatcher()).getReqs();
		if (!reqs.isEmpty()) {
			reqs.stream().collect(Collectors.toSet()).stream()
					.map((String req) -> Requirement.newBuilder().setId(req).build())
					.forEach((Requirement req) -> publisher.publish(req));

		}
		return zuper.call();
	}
}