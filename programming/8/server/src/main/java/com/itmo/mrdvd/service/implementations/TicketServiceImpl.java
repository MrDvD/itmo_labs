package com.itmo.mrdvd.service.implementations;

import com.itmo.mrdvd.AddTicketResponse;
import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.CollectionMeta;
import com.itmo.mrdvd.GetInfoRequest;
import com.itmo.mrdvd.GetTicketsRequest;
import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.ObjectId;
import com.itmo.mrdvd.RemoveTicketRequest;
import com.itmo.mrdvd.RemoveTicketResponse;
import com.itmo.mrdvd.TicketServiceGrpc.TicketServiceImplBase;
import com.itmo.mrdvd.UpdateTicketRequest;
import com.itmo.mrdvd.UpdateTicketResponse;
import com.itmo.mrdvd.UserInfo;
import com.itmo.mrdvd.UserServiceGrpc.UserServiceImplBase;
import com.itmo.mrdvd.collection.ticket.NodeComparator;
import com.itmo.mrdvd.collection.ticket.TicketComparator;
import com.itmo.mrdvd.mappers.Mapper;
import com.itmo.mrdvd.object.TicketField;
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
      GetInfoRequest request,
      io.grpc.stub.StreamObserver<com.itmo.mrdvd.CollectionMeta> responseObserver) {
    Object result = this.exec.processCommand("info", List.of());
    if (result instanceof CollectionMeta infoMeta) {
      responseObserver.onNext(infoMeta);
    } else {
      responseObserver.onNext(CollectionMeta.getDefaultInstance());
    }
    responseObserver.onCompleted();
  }

  @Override
  public void getTickets(
      GetTicketsRequest request, io.grpc.stub.StreamObserver<Node> responseObserver) {
    Object result = this.exec.processCommand("show", List.of());
    if (result != null) {
      try {
        Set<Node> tickets = (Set) result;
        tickets.stream()
            .sorted(new NodeComparator(new TicketComparator(TicketField.CREATION_DATE)))
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
      Node request, io.grpc.stub.StreamObserver<AddTicketResponse> responseObserver) {
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
            responseObserver.onNext(AddTicketResponse.getDefaultInstance());
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
      UpdateTicketRequest request,
      io.grpc.stub.StreamObserver<UpdateTicketResponse> responseObserver) {
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
            Object toUpdate = exec.processCommand("show_by_id", List.of(request.getId().getId()));
            if (toUpdate != null && toUpdate instanceof Optional opt && opt.isPresent()) {
              Node node = (Node) opt.get();
              if (node.getAuthor().equals(info.getUsername())) {
                exec.processCommand(
                    "update",
                    List.of(
                        node.getItem().getTicket().getId().getId(),
                        request.getNode().toBuilder().setAuthor(info.getUsername()).build()));
                responseObserver.onNext(UpdateTicketResponse.getDefaultInstance());
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
  public void getTicket(ObjectId request, io.grpc.stub.StreamObserver<Node> responseObserver) {
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
  public void removeTicket(
      RemoveTicketRequest request,
      io.grpc.stub.StreamObserver<RemoveTicketResponse> responseObserver) {
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
            Object toRemove = null;
            switch (request.getHeaderCase()) {
              case ID:
                toRemove = exec.processCommand("show_by_id", List.of(request.getId().getId()));
                break;
              case IDX:
                toRemove = exec.processCommand("show_at", List.of(request.getIdx()));
                break;
              case HEADER_NOT_SET:
                toRemove = exec.processCommand("show_last", List.of());
                break;
            }
            if (toRemove != null && toRemove instanceof Optional opt && opt.isPresent()) {
              Node node = (Node) opt.get();
              if (node.getAuthor().equals(info.getUsername())) {
                exec.processCommand(
                    "remove_by_id", List.of(node.getItem().getTicket().getId().getId()));
                responseObserver.onNext(RemoveTicketResponse.getDefaultInstance());
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
