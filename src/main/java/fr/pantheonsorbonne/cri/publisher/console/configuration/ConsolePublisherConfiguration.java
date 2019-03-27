package fr.pantheonsorbonne.cri.publisher.console.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.console.impl.ConsoleRequirementsPublisher;
import fr.pantheonsorbonne.cri.publisher.grpc.impl.DummyObserver;
import fr.pantheonsorbonne.cri.publisher.grpc.impl.GrPCRequirementPublisher;
import fr.pantheonsorbonne.cri.requirements.Empty;
import fr.pantheonsorbonne.cri.requirements.ReqCollectorGrpc;
import fr.pantheonsorbonne.cri.requirements.Requirement;
import fr.pantheonsorbonne.cri.requirements.ReqCollectorGrpc.ReqCollectorStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ConsolePublisherConfiguration extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();
		this.bind(RequirementPublisher.class).to(ConsoleRequirementsPublisher.class);

	}

}
