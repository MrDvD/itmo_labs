package com.itmo.mrdvd.service.interceptors;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public abstract class FillContextInterceptor implements ServerInterceptor {
  protected abstract Context fillContext(Metadata meta);

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    Context ctx = this.fillContext(headers);
    return Contexts.interceptCall(ctx, serverCall, headers, next);
  }
}
