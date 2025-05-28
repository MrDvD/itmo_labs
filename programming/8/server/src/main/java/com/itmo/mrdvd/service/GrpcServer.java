package com.itmo.mrdvd.service;

import com.itmo.mrdvd.service.executor.AbstractExecutor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class GrpcServer implements Service {
  private final Server server;

  public GrpcServer(ServerBuilder<?> serverBuilder, AbstractExecutor exec) {
    this.server =
        serverBuilder
            .addService(new TicketServiceImpl(exec))
            // .addService(ProtoReflectionServiceV1.newInstance())
            .build();
  }

  @Override
  public void start() {
    try {
      this.server.start();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void block() {
    try {
      this.server.awaitTermination();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    this.server.shutdown();
  }
}
