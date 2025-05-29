package com.itmo.mrdvd.service;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public class ExceptionInterceptor implements ServerInterceptor {
  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    ServerCall.Listener<ReqT> delegate = null;
    System.out.println("interception!");
    try {
      delegate = next.startCall(serverCall, headers);
    } catch (Exception ex) {
      serverCall.close(
          Status.INTERNAL
              .withCause(ex)
              .withDescription("An exception occurred in a **subsequent** interceptor:"),
          new Metadata());
    }
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
      @Override
      public void onHalfClose() {
        try {
          super.onHalfClose();
        } catch (Exception ex) {
          handleEndpointException(ex, serverCall);
        }
      }
    };
  }

  private static <ReqT, RespT> void handleEndpointException(
      Exception ex, ServerCall<ReqT, RespT> serverCall) {
    serverCall.close(
        Status.INTERNAL.withCause(ex).withDescription(ex.getMessage()), new Metadata());
  }
}
