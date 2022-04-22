package fr.pantheonsorbonne.cri.publisher.grpc;

import com.google.inject.Inject;
import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.model.requirements.AgentConfig;
import fr.pantheonsorbonne.cri.model.requirements.AgentConfigurationGrpc;
import fr.pantheonsorbonne.cri.model.requirements.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

public class GrPCConfigurationPublisher {

    @Inject
    ManagedChannel chan;

    @Inject
    RequirementIssueDecorator requirementIssueDecorator;

    void publish() {
        AgentConfigurationGrpc.newStub(chan).pushAgentConfiguration(AgentConfig.newBuilder()
                        .setProjectGHPath(requirementIssueDecorator.getRoot()).setProjectName("project1").build(),
                new StreamObserver<Empty>() {

                    @Override
                    public void onNext(Empty value) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(Throwable t) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onCompleted() {
                        // TODO Auto-generated method stub

                    }
                }

        );
    }

}
