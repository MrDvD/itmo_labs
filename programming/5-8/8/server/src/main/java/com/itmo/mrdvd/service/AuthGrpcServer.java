package com.itmo.mrdvd.service;

import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.mappers.Mapper;
import com.itmo.mrdvd.service.implementations.AuthServiceImpl;
import io.grpc.Metadata;
import io.grpc.ServerBuilder;

public class AuthGrpcServer extends AbstractGrpcServer {
  public AuthGrpcServer(
      ServerBuilder<?> serverBuilder,
      AuthServiceImpl authService,
      Mapper<Metadata, AuthID> idMapper) {
    this.server = serverBuilder.addService(authService).build();
  }
}
