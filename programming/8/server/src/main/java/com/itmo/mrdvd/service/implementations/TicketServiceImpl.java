package com.itmo.mrdvd.service.implementations;

import com.google.protobuf.Empty;
import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.Meta;
import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.TicketServiceGrpc.TicketServiceImplBase;
import com.itmo.mrdvd.UserInfo;
import com.itmo.mrdvd.UserServiceGrpc.UserServiceImplBase;
import com.itmo.mrdvd.mappers.Mapper;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TicketServiceImpl extends TicketServiceImplBase {
  private final AbstractExecutor exec;
  private final UserServiceImplBase userService;
  private final Mapper<Context, AuthID> idMapper;

  public TicketServiceImpl(
      AbstractExecutor exec, UserServiceImplBase userService, Mapper<Context, AuthID> idMapper) {
    this.exec = exec;
    this.userService = userService;
    this.idMapper = idMapper;
  }

  @Override
  public void getInfo(
      com.google.protobuf.Empty request,
      io.grpc.stub.StreamObserver<com.itmo.mrdvd.Meta> responseObserver) {
    Object result = this.exec.processCommand("info", List.of());
    if (result instanceof Meta infoMeta) {
      responseObserver.onNext(infoMeta);
    } else {
      responseObserver.onNext(Meta.getDefaultInstance());
    }
    responseObserver.onCompleted();
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
    Optional<AuthID> id = this.idMapper.convert(Context.current());
    if (id.isEmpty()) {
      responseObserver.onError(Status.INTERNAL.asRuntimeException());
      return;
    }
    this.userService.getUserInfo(
        id.get(),
        new StreamObserver<UserInfo>() {
          @Override
          public void onNext(UserInfo info) {
            if (info == null) {
              responseObserver.onError(Status.INTERNAL.asRuntimeException());
              return;
            }
            exec.processCommand(
                "add", List.of(request.toBuilder().setAuthor(info.getUsername()).build()));
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
          }

          @Override
          public void onError(Throwable e) {}

          @Override
          public void onCompleted() {}
        });
  }

  @Override
  public void updateTicket(
      com.itmo.mrdvd.UpdateId request,
      io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
    Optional<AuthID> id = this.idMapper.convert(Context.current());
    if (id.isEmpty()) {
      responseObserver.onError(Status.INTERNAL.asRuntimeException());
      return;
    }
    this.userService.getUserInfo(
        id.get(),
        new StreamObserver<UserInfo>() {
          @Override
          public void onNext(UserInfo info) {
            if (info == null) {
              responseObserver.onError(Status.INTERNAL.asRuntimeException());
              return;
            }
            Object toUpdate = exec.processCommand("show_by_id", List.of(request.getId()));
            if (toUpdate != null && toUpdate instanceof Optional opt && opt.isPresent()) {
              Node node = (Node) opt.get();
              if (node.getAuthor().equals(info.getUsername())) {
                exec.processCommand(
                    "update", List.of(node.getItem().getTicket().getId(), request.getNode()));
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
              } else {
                responseObserver.onError(
                    Status.PERMISSION_DENIED
                        .withDescription("Невозможно изменить чужой объект.")
                        .asRuntimeException());
              }
            } else {
              responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
            }
          }

          @Override
          public void onError(Throwable e) {}

          @Override
          public void onCompleted() {}
        });
    this.exec.processCommand("update", List.of(request.getId().getId(), request.getNode()));
    responseObserver.onNext(Empty.getDefaultInstance());
    responseObserver.onCompleted();
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

  @Override
  public void removeLast(
      com.google.protobuf.Empty request,
      io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
    Optional<AuthID> id = this.idMapper.convert(Context.current());
    if (id.isEmpty()) {
      responseObserver.onError(Status.INTERNAL.asRuntimeException());
      return;
    }
    this.userService.getUserInfo(
        id.get(),
        new StreamObserver<UserInfo>() {
          @Override
          public void onNext(UserInfo info) {
            if (info == null) {
              responseObserver.onError(Status.INTERNAL.asRuntimeException());
              return;
            }
            Object toRemove = exec.processCommand("show_last", List.of());
            if (toRemove != null && toRemove instanceof Optional opt && opt.isPresent()) {
              Node node = (Node) opt.get();
              if (node.getAuthor().equals(info.getUsername())) {
                exec.processCommand("remove_by_id", List.of(node.getItem().getTicket().getId()));
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
              } else {
                responseObserver.onError(
                    Status.PERMISSION_DENIED
                        .withDescription("Невозможно изменить чужой объект.")
                        .asRuntimeException());
              }
            } else {
              responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
            }
          }

          @Override
          public void onError(Throwable e) {}

          @Override
          public void onCompleted() {}
        });
  }

  @Override
  public void removeAt(
      com.itmo.mrdvd.IntId request,
      io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
    Optional<AuthID> id = this.idMapper.convert(Context.current());
    if (id.isEmpty()) {
      responseObserver.onError(Status.INTERNAL.asRuntimeException());
      return;
    }
    this.userService.getUserInfo(
        id.get(),
        new StreamObserver<UserInfo>() {
          @Override
          public void onNext(UserInfo info) {
            if (info == null) {
              responseObserver.onError(Status.INTERNAL.asRuntimeException());
              return;
            }
            Object toRemove = exec.processCommand("show_at", List.of(request.getId()));
            if (toRemove != null && toRemove instanceof Optional opt && opt.isPresent()) {
              Node node = (Node) opt.get();
              if (node.getAuthor().equals(info.getUsername())) {
                exec.processCommand("remove_by_id", List.of(node.getItem().getTicket().getId()));
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
              } else {
                responseObserver.onError(
                    Status.PERMISSION_DENIED
                        .withDescription("Невозможно изменить чужой объект.")
                        .asRuntimeException());
              }
            } else {
              responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
            }
          }

          @Override
          public void onError(Throwable e) {}

          @Override
          public void onCompleted() {}
        });
  }

  @Override
  public void removeById(
      com.itmo.mrdvd.LongId request,
      io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
    Optional<AuthID> id = this.idMapper.convert(Context.current());
    if (id.isEmpty()) {
      responseObserver.onError(Status.INTERNAL.asRuntimeException());
      return;
    }
    this.userService.getUserInfo(
        id.get(),
        new StreamObserver<UserInfo>() {
          @Override
          public void onNext(UserInfo info) {
            if (info == null) {
              responseObserver.onError(Status.INTERNAL.asRuntimeException());
              return;
            }
            Object toRemove = exec.processCommand("show_by_id", List.of(request.getId()));
            if (toRemove != null && toRemove instanceof Optional opt && opt.isPresent()) {
              Node node = (Node) opt.get();
              if (node.getAuthor().equals(info.getUsername())) {
                exec.processCommand("remove_by_id", List.of(node.getItem().getTicket().getId()));
                responseObserver.onNext(Empty.getDefaultInstance());
                responseObserver.onCompleted();
              } else {
                responseObserver.onError(
                    Status.PERMISSION_DENIED
                        .withDescription("Невозможно изменить чужой объект.")
                        .asRuntimeException());
              }
            } else {
              responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
            }
          }

          @Override
          public void onError(Throwable e) {}

          @Override
          public void onCompleted() {}
        });
  }
}
