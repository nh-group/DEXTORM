package fr.pantheonsorbonne.cri.publisher.grpc;

import com.google.inject.Inject;
import fr.pantheonsorbonne.cri.model.requirements.Empty;
import fr.pantheonsorbonne.cri.model.requirements.ReqCollectorGrpc.ReqCollectorStub;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.AbstractRequirementPublisher;
import io.grpc.stub.StreamObserver;

public class GrPCRequirementPublisher extends AbstractRequirementPublisher {

    final io.grpc.stub.StreamObserver<Requirement> reqPublisher;

    @Inject
    public GrPCRequirementPublisher(ReqCollectorStub service, StreamObserver<Empty> obs) {
        reqPublisher = service.pushRequirement(obs);
    }

    @Override
    protected void publishLinkedRequirement(Requirement req) {
        reqPublisher.onNext(req);

    }

}
