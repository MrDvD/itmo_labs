package com.itmo.mrdvd.service.interceptors;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import java.util.function.Function;

public class FillContextInterceptor implements ServerInterceptor {
  private final Function<Metadata, Context> fillContext;

  public FillContextInterceptor(Function<Metadata, Context> fillContext) {
    this.fillContext = fillContext;
  }

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
    Context ctx = this.fillContext.apply(headers);
    return Contexts.interceptCall(ctx, serverCall, headers, next);
  }
}
