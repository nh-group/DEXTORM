package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.*;
import fr.pantheonsorbonne.cri.model.requirements.Empty;
import fr.pantheonsorbonne.cri.model.requirements.ReqCollectorGrpc;
import fr.pantheonsorbonne.cri.model.requirements.ReqCollectorGrpc.ReqCollectorStub;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.grpc.DummyObserver;
import fr.pantheonsorbonne.cri.publisher.grpc.GrPCRequirementPublisher;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class GRPCPublisherConfigurationModule extends AbstractModule {

    String grpcHost;
    int grpcPort;

    public GRPCPublisherConfigurationModule(String grpcHost, int grpcPort) {
        this.grpcHost = grpcHost;
        this.grpcPort = grpcPort;
    }

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
    public ManagedChannel getChannel() {
        return ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext()
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
