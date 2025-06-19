package com.itmo.mrdvd.service.implementations;

import com.itmo.mrdvd.AuthResponse;
import com.itmo.mrdvd.AuthServiceGrpc.AuthServiceImplBase;
import com.itmo.mrdvd.AuthStatus;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.List;

public class AuthServiceImpl extends AuthServiceImplBase {
  private final AbstractExecutor exec;

  public AuthServiceImpl(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public void register(
      com.itmo.mrdvd.Credentials request,
      io.grpc.stub.StreamObserver<com.itmo.mrdvd.AuthResponse> responseObserver) {
    try {
      this.exec.processCommand("register", List.of(request));
      responseObserver.onNext(AuthResponse.newBuilder().setStatus(AuthStatus.REGISTERED).build());
    } catch (RuntimeException e) {
      responseObserver.onNext(AuthResponse.newBuilder().setStatus(AuthStatus.INVALID).build());
    } finally {
      responseObserver.onCompleted();
    }
  }

  @Override
  public void login(
      com.itmo.mrdvd.Credentials request,
      io.grpc.stub.StreamObserver<com.itmo.mrdvd.AuthResponse> responseObserver) {
    Object response = this.exec.processCommand("login", List.of(request));
    if (response instanceof AuthResponse authResponse) {
      responseObserver.onNext(authResponse);
    } else {
      responseObserver.onNext(AuthResponse.getDefaultInstance());
    }
    responseObserver.onCompleted();
  }

  @Override
  public void check(
      com.itmo.mrdvd.AuthID request,
      io.grpc.stub.StreamObserver<com.itmo.mrdvd.AuthResponse> responseObserver) {
    Object response = this.exec.processCommand("validate_login", List.of(request));
    if (response instanceof AuthResponse authResponse) {
      responseObserver.onNext(authResponse);
    } else {
      responseObserver.onNext(AuthResponse.getDefaultInstance());
    }
    responseObserver.onCompleted();
  }
}
