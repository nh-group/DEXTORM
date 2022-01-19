package fr.pantheonsorbonne.cri.requirements;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.18.0)",
    comments = "Source: proto/requirements.proto")
public final class AgentConfigurationGrpc {

  private AgentConfigurationGrpc() {}

  public static final String SERVICE_NAME = "requirements.AgentConfiguration";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<AgentConfig,
          Empty> getPushAgentConfigurationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "pushAgentConfiguration",
      requestType = AgentConfig.class,
      responseType = Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<AgentConfig,
          Empty> getPushAgentConfigurationMethod() {
    io.grpc.MethodDescriptor<AgentConfig, Empty> getPushAgentConfigurationMethod;
    if ((getPushAgentConfigurationMethod = AgentConfigurationGrpc.getPushAgentConfigurationMethod) == null) {
      synchronized (AgentConfigurationGrpc.class) {
        if ((getPushAgentConfigurationMethod = AgentConfigurationGrpc.getPushAgentConfigurationMethod) == null) {
          AgentConfigurationGrpc.getPushAgentConfigurationMethod = getPushAgentConfigurationMethod =
              io.grpc.MethodDescriptor.<AgentConfig, Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "requirements.AgentConfiguration", "pushAgentConfiguration"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  AgentConfig.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Empty.getDefaultInstance()))
                  .setSchemaDescriptor(new AgentConfigurationMethodDescriptorSupplier("pushAgentConfiguration"))
                  .build();
          }
        }
     }
     return getPushAgentConfigurationMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AgentConfigurationStub newStub(io.grpc.Channel channel) {
    return new AgentConfigurationStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AgentConfigurationBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AgentConfigurationBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AgentConfigurationFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AgentConfigurationFutureStub(channel);
  }

  /**
   */
  public static abstract class AgentConfigurationImplBase implements io.grpc.BindableService {

    /**
     */
    public void pushAgentConfiguration(AgentConfig request,
                                       io.grpc.stub.StreamObserver<Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getPushAgentConfigurationMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPushAgentConfigurationMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                      AgentConfig,
                      Empty>(
                  this, METHODID_PUSH_AGENT_CONFIGURATION)))
          .build();
    }
  }

  /**
   */
  public static final class AgentConfigurationStub extends io.grpc.stub.AbstractStub<AgentConfigurationStub> {
    private AgentConfigurationStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AgentConfigurationStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected AgentConfigurationStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AgentConfigurationStub(channel, callOptions);
    }

    /**
     */
    public void pushAgentConfiguration(AgentConfig request,
                                       io.grpc.stub.StreamObserver<Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPushAgentConfigurationMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AgentConfigurationBlockingStub extends io.grpc.stub.AbstractStub<AgentConfigurationBlockingStub> {
    private AgentConfigurationBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AgentConfigurationBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected AgentConfigurationBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AgentConfigurationBlockingStub(channel, callOptions);
    }

    /**
     */
    public Empty pushAgentConfiguration(AgentConfig request) {
      return blockingUnaryCall(
          getChannel(), getPushAgentConfigurationMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AgentConfigurationFutureStub extends io.grpc.stub.AbstractStub<AgentConfigurationFutureStub> {
    private AgentConfigurationFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AgentConfigurationFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected AgentConfigurationFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AgentConfigurationFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Empty> pushAgentConfiguration(
        AgentConfig request) {
      return futureUnaryCall(
          getChannel().newCall(getPushAgentConfigurationMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PUSH_AGENT_CONFIGURATION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AgentConfigurationImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AgentConfigurationImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PUSH_AGENT_CONFIGURATION:
          serviceImpl.pushAgentConfiguration((AgentConfig) request,
              (io.grpc.stub.StreamObserver<Empty>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class AgentConfigurationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AgentConfigurationBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Requirements.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AgentConfiguration");
    }
  }

  private static final class AgentConfigurationFileDescriptorSupplier
      extends AgentConfigurationBaseDescriptorSupplier {
    AgentConfigurationFileDescriptorSupplier() {}
  }

  private static final class AgentConfigurationMethodDescriptorSupplier
      extends AgentConfigurationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AgentConfigurationMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AgentConfigurationGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AgentConfigurationFileDescriptorSupplier())
              .addMethod(getPushAgentConfigurationMethod())
              .build();
        }
      }
    }
    return result;
  }
}
