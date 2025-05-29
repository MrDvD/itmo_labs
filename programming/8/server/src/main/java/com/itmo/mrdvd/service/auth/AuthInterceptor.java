package com.itmo.mrdvd.service.auth;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.function.Predicate;

public class AuthInterceptor implements ServerInterceptor {
  private final Predicate<Metadata> isAuthenticated;

  public AuthInterceptor(Predicate<Metadata> isAuthenticated) {
    this.isAuthenticated = isAuthenticated;
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    if (!this.isAuthenticated.test(headers)) {
      serverCall.close(Status.PERMISSION_DENIED, headers);
    }
    return next.startCall(serverCall, headers);
  }
}
