package com.itmo.mrdvd.service.interceptors;

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
    ServerCall.Listener<ReqT> delegate = next.startCall(serverCall, headers);
    return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
      @Override
      public void onMessage(ReqT message) {
        try {
          super.onMessage(message);
        } catch (RuntimeException e) {
          handleException(e);
        }
      }

      @Override
      public void onHalfClose() {
        try {
          super.onHalfClose();
        } catch (RuntimeException e) {
          handleException(e);
        }
      }

      private void handleException(RuntimeException e) {
        serverCall.close(
            Status.INTERNAL.withDescription(e.getMessage()).withCause(e), new Metadata());
      }
    };
  }
}
