package fr.pantheonsorbonne.cri.publisher.grpc.impl;

import com.google.inject.Inject;

import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.requirements.AgentConfig;
import fr.pantheonsorbonne.cri.requirements.AgentConfigurationGrpc;
import fr.pantheonsorbonne.cri.requirements.Empty;
import fr.pantheonsorbonne.cri.requirements.AgentConfigurationGrpc.AgentConfigurationBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.internal.NoopClientStream;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;

public class GrPCConfigurationPublisher {

	@Inject
	ApplicationParameters params;
	@Inject
	ManagedChannel chan;

	void publish() {
		AgentConfigurationGrpc.newStub(chan).pushAgentConfiguration(AgentConfig.newBuilder()
				.setProjectGHPath(params.getRequirementIssueDecorator().getRoot()).setProjectName("project1").build(),
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
	};

}
