package com.itmo.mrdvd.service;

import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.AuthServiceGrpc.AuthServiceImplBase;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.implementations.TicketServiceImpl;
import com.itmo.mrdvd.service.interceptors.ExceptionInterceptor;
import com.itmo.mrdvd.service.interceptors.KeyFillContextInterceptor;
import com.itmo.mrdvd.service.interceptors.TokenAuthInterceptor;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.ServerBuilder;

public class TicketGrpcServer extends AbstractGrpcServer {
  public TicketGrpcServer(
      ServerBuilder<?> serverBuilder,
      TicketServiceImpl ticketService,
      AuthServiceImplBase authService,
      Mapper<Metadata, AuthID> idMapper,
      Context.Key<Object> tokenKey) {
    this.server =
        serverBuilder
            .addService(ticketService)
            .intercept(new KeyFillContextInterceptor(tokenKey))
            .intercept(new TokenAuthInterceptor(authService, idMapper))
            .intercept(new ExceptionInterceptor())
            .build();
  }
}
