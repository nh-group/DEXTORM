package fr.pantheonsorbonne.cri.publisher.grpc.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;

import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.configuration.variables.DemoApplicationParameters;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.grpc.impl.DummyObserver;
import fr.pantheonsorbonne.cri.publisher.grpc.impl.GrPCRequirementPublisher;
import fr.pantheonsorbonne.cri.requirements.AgentConfigurationGrpc.AgentConfigurationBlockingStub;
import fr.pantheonsorbonne.cri.requirements.Empty;
import fr.pantheonsorbonne.cri.requirements.ReqCollectorGrpc;
import fr.pantheonsorbonne.cri.requirements.Requirement;
import fr.pantheonsorbonne.cri.requirements.ReqCollectorGrpc.ReqCollectorStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GRPCPublisherConfiguration extends AbstractModule {

	@Override
	protected void configure() {
		super.configure();
		this.bind(RequirementPublisher.class).to(GrPCRequirementPublisher.class);
		bind(new TypeLiteral<StreamObserver<Empty>>() {
		}).to(new TypeLiteral<DummyObserver>() {
		});

	}

	@Provides
	@Singleton
	@Inject
	public ReqCollectorStub getStud(ManagedChannel chan) {
		return ReqCollectorGrpc.newStub(chan);
	}

	@Provides
	@Singleton
	@Inject
	public ManagedChannel getChannel(DemoApplicationParameters vars) {
		return ManagedChannelBuilder.forAddress(vars.getGRPEndpointHost(), vars.getGRPCEndpointPort()).usePlaintext()
				.build();

	}

	@Inject
	@Singleton
	@Provides
	public StreamObserver<Requirement> initGrpcChannel(ManagedChannel channel, StreamObserver<Empty> obs) {

		ReqCollectorStub stub = ReqCollectorGrpc.newStub(channel);
		return stub.pushRequirement(obs);

	}
	
	

}
