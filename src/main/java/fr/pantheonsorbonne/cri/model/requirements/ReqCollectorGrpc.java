package fr.pantheonsorbonne.cri.model.requirements;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.*;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.*;

/**
 *
 */
@javax.annotation.Generated(
        value = "by gRPC proto compiler (version 1.18.0)",
        comments = "Source: proto/requirements.proto")
public final class ReqCollectorGrpc {

    public static final String SERVICE_NAME = "requirements.ReqCollector";
    private static final int METHODID_PULL_REQUIREMENT = 0;
    private static final int METHODID_PUSH_REQUIREMENT = 1;
    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<Requirement,
            Empty> getPushRequirementMethod;
    private static volatile io.grpc.MethodDescriptor<Empty,
            Requirement> getPullRequirementMethod;
    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    private ReqCollectorGrpc() {
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static ReqCollectorStub newStub(io.grpc.Channel channel) {
        return new ReqCollectorStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static ReqCollectorBlockingStub newBlockingStub(
            io.grpc.Channel channel) {
        return new ReqCollectorBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static ReqCollectorFutureStub newFutureStub(
            io.grpc.Channel channel) {
        return new ReqCollectorFutureStub(channel);
    }

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (ReqCollectorGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                            .setSchemaDescriptor(new ReqCollectorFileDescriptorSupplier())
                            .addMethod(getPushRequirementMethod())
                            .addMethod(getPullRequirementMethod())
                            .build();
                }
            }
        }
        return result;
    }

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "pushRequirement",
            requestType = Requirement.class,
            responseType = Empty.class,
            methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
    public static io.grpc.MethodDescriptor<Requirement,
            Empty> getPushRequirementMethod() {
        io.grpc.MethodDescriptor<Requirement, Empty> getPushRequirementMethod;
        if ((getPushRequirementMethod = ReqCollectorGrpc.getPushRequirementMethod) == null) {
            synchronized (ReqCollectorGrpc.class) {
                if ((getPushRequirementMethod = ReqCollectorGrpc.getPushRequirementMethod) == null) {
                    ReqCollectorGrpc.getPushRequirementMethod = getPushRequirementMethod =
                            io.grpc.MethodDescriptor.<Requirement, Empty>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
                                    .setFullMethodName(generateFullMethodName(
                                            "requirements.ReqCollector", "pushRequirement"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            Requirement.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            Empty.getDefaultInstance()))
                                    .setSchemaDescriptor(new ReqCollectorMethodDescriptorSupplier("pushRequirement"))
                                    .build();
                }
            }
        }
        return getPushRequirementMethod;
    }

    @io.grpc.stub.annotations.RpcMethod(
            fullMethodName = SERVICE_NAME + '/' + "pullRequirement",
            requestType = Empty.class,
            responseType = Requirement.class,
            methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
    public static io.grpc.MethodDescriptor<Empty,
            Requirement> getPullRequirementMethod() {
        io.grpc.MethodDescriptor<Empty, Requirement> getPullRequirementMethod;
        if ((getPullRequirementMethod = ReqCollectorGrpc.getPullRequirementMethod) == null) {
            synchronized (ReqCollectorGrpc.class) {
                if ((getPullRequirementMethod = ReqCollectorGrpc.getPullRequirementMethod) == null) {
                    ReqCollectorGrpc.getPullRequirementMethod = getPullRequirementMethod =
                            io.grpc.MethodDescriptor.<Empty, Requirement>newBuilder()
                                    .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
                                    .setFullMethodName(generateFullMethodName(
                                            "requirements.ReqCollector", "pullRequirement"))
                                    .setSampledToLocalTracing(true)
                                    .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            Empty.getDefaultInstance()))
                                    .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                                            Requirement.getDefaultInstance()))
                                    .setSchemaDescriptor(new ReqCollectorMethodDescriptorSupplier("pullRequirement"))
                                    .build();
                }
            }
        }
        return getPullRequirementMethod;
    }

    /**
     *
     */
    public static abstract class ReqCollectorImplBase implements io.grpc.BindableService {

        /**
         *
         */
        public io.grpc.stub.StreamObserver<Requirement> pushRequirement(
                io.grpc.stub.StreamObserver<Empty> responseObserver) {
            return asyncUnimplementedStreamingCall(getPushRequirementMethod(), responseObserver);
        }

        /**
         *
         */
        public void pullRequirement(Empty request,
                                    io.grpc.stub.StreamObserver<Requirement> responseObserver) {
            asyncUnimplementedUnaryCall(getPullRequirementMethod(), responseObserver);
        }

        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                    .addMethod(
                            getPushRequirementMethod(),
                            asyncClientStreamingCall(
                                    new MethodHandlers<
                                            Requirement,
                                            Empty>(
                                            this, METHODID_PUSH_REQUIREMENT)))
                    .addMethod(
                            getPullRequirementMethod(),
                            asyncServerStreamingCall(
                                    new MethodHandlers<
                                            Empty,
                                            Requirement>(
                                            this, METHODID_PULL_REQUIREMENT)))
                    .build();
        }
    }

    /**
     *
     */
    public static final class ReqCollectorStub extends io.grpc.stub.AbstractStub<ReqCollectorStub> {
        private ReqCollectorStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ReqCollectorStub(io.grpc.Channel channel,
                                 io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected ReqCollectorStub build(io.grpc.Channel channel,
                                         io.grpc.CallOptions callOptions) {
            return new ReqCollectorStub(channel, callOptions);
        }

        /**
         *
         */
        public io.grpc.stub.StreamObserver<Requirement> pushRequirement(
                io.grpc.stub.StreamObserver<Empty> responseObserver) {
            return asyncClientStreamingCall(
                    getChannel().newCall(getPushRequirementMethod(), getCallOptions()), responseObserver);
        }

        /**
         *
         */
        public void pullRequirement(Empty request,
                                    io.grpc.stub.StreamObserver<Requirement> responseObserver) {
            asyncServerStreamingCall(
                    getChannel().newCall(getPullRequirementMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     *
     */
    public static final class ReqCollectorBlockingStub extends io.grpc.stub.AbstractStub<ReqCollectorBlockingStub> {
        private ReqCollectorBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ReqCollectorBlockingStub(io.grpc.Channel channel,
                                         io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected ReqCollectorBlockingStub build(io.grpc.Channel channel,
                                                 io.grpc.CallOptions callOptions) {
            return new ReqCollectorBlockingStub(channel, callOptions);
        }

        /**
         *
         */
        public java.util.Iterator<Requirement> pullRequirement(
                Empty request) {
            return blockingServerStreamingCall(
                    getChannel(), getPullRequirementMethod(), getCallOptions(), request);
        }
    }

    /**
     *
     */
    public static final class ReqCollectorFutureStub extends io.grpc.stub.AbstractStub<ReqCollectorFutureStub> {
        private ReqCollectorFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private ReqCollectorFutureStub(io.grpc.Channel channel,
                                       io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected ReqCollectorFutureStub build(io.grpc.Channel channel,
                                               io.grpc.CallOptions callOptions) {
            return new ReqCollectorFutureStub(channel, callOptions);
        }
    }

    private static final class MethodHandlers<Req, Resp> implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final ReqCollectorImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(ReqCollectorImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_PULL_REQUIREMENT:
                    serviceImpl.pullRequirement((Empty) request,
                            (io.grpc.stub.StreamObserver<Requirement>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
                io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_PUSH_REQUIREMENT:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.pushRequirement(
                            (io.grpc.stub.StreamObserver<Empty>) responseObserver);
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class ReqCollectorBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        ReqCollectorBaseDescriptorSupplier() {
        }

        @Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("ReqCollector");
        }

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return Requirements.getDescriptor();
        }
    }

    private static final class ReqCollectorFileDescriptorSupplier
            extends ReqCollectorBaseDescriptorSupplier {
        ReqCollectorFileDescriptorSupplier() {
        }
    }

    private static final class ReqCollectorMethodDescriptorSupplier
            extends ReqCollectorBaseDescriptorSupplier
            implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        ReqCollectorMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }
}
