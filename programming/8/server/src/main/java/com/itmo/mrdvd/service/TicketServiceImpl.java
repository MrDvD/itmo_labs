package com.itmo.mrdvd.service;

import com.google.protobuf.Empty;
import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.TicketServiceGrpc.TicketServiceImplBase;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TicketServiceImpl extends TicketServiceImplBase {
  private final AbstractExecutor exec;

  public TicketServiceImpl(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public void getTickets(
      com.google.protobuf.Empty request, io.grpc.stub.StreamObserver<Node> responseObserver) {
    Object result = this.exec.processCommand("show", List.of());
    if (result != null) {
      try {
        Set<Node> tickets = (Set) result;
        tickets.stream()
            .forEach(
                ticket -> {
                  responseObserver.onNext(ticket);
                });
      } catch (ClassCastException e) {
        throw new RuntimeException(e);
      }
    }
    responseObserver.onCompleted();
  }

  @Override
  public void addTicket(
      Node request, io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
    try {
      this.exec.processCommand("add", List.of(request));
      responseObserver.onNext(Empty.getDefaultInstance());
      responseObserver.onCompleted();
    } catch (RuntimeException e) {
      responseObserver.onError(e);
    }
  }

  @Override
  public void getTicket(
      com.itmo.mrdvd.LongId request, io.grpc.stub.StreamObserver<Node> responseObserver) {
    try {
      Object raw = this.exec.processCommand("show_by_id", List.of(request.getId()));
      if (raw instanceof Optional result) {
        if (result.isPresent()) {
          if (result.get() instanceof Node n) {
            responseObserver.onNext(n);
            return;
          }
        }
      }
      responseObserver.onNext(Node.getDefaultInstance());
    } finally {
      responseObserver.onCompleted();
    }
  }
}
