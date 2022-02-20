package fr.pantheonsorbonne.cri.publisher.grpc;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;

import fr.pantheonsorbonne.cri.model.requirements.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

public class DummyObserver implements StreamObserver<Empty> {

	@Inject
	Logger logger;

	@Inject
	ManagedChannel chan;

	@Override
	public void onNext(Empty value) {
		// noop

	}

	@Override
	public void onError(Throwable t) {
		logger.log(Level.ALL, "GrPC channel closed out of error", t);
		System.exit(-1);

	}

	@Override
	public void onCompleted() {

		logger.log(Level.ALL, "GrPC channel closed out of completion");

	}
}