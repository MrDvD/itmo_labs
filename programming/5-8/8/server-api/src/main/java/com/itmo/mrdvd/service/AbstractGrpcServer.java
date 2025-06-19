package com.itmo.mrdvd.service;

import io.grpc.Server;
import java.io.IOException;

public abstract class AbstractGrpcServer implements Service {
  protected Server server;

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
