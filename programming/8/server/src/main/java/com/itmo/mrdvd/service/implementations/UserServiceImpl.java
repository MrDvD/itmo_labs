package com.itmo.mrdvd.service.implementations;

import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.UserInfo;
import com.itmo.mrdvd.UserServiceGrpc.UserServiceImplBase;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import io.grpc.Status;
import java.util.Optional;

public class UserServiceImpl extends UserServiceImplBase {
  private final AbstractExecutor exec;
  private final Mapper<AuthID, UserInfo> mapper;

  public UserServiceImpl(AbstractExecutor exec, Mapper<AuthID, UserInfo> mapper) {
    this.exec = exec;
    this.mapper = mapper;
  }

  @Override
  public void getUserInfo(
      com.itmo.mrdvd.AuthID request,
      io.grpc.stub.StreamObserver<com.itmo.mrdvd.UserInfo> responseObserver) {
    Optional<UserInfo> info = this.mapper.convert(request);
    if (info.isEmpty()) {
      responseObserver.onError(Status.PERMISSION_DENIED.asRuntimeException());
      return;
    }
    responseObserver.onNext(info.get());
    responseObserver.onCompleted();
  }
}
