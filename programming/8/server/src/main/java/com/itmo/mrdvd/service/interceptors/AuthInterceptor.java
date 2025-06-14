package com.itmo.mrdvd.service.interceptors;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;

public abstract class AuthInterceptor implements ServerInterceptor {
  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    if (!this.authCheck(headers)) {
      serverCall.close(Status.PERMISSION_DENIED.withDescription("Unauthorized"), headers);
      return new ServerCall.Listener<ReqT>() {};
    }
    return next.startCall(serverCall, headers);
  }

  protected abstract boolean authCheck(Metadata headers);
}
