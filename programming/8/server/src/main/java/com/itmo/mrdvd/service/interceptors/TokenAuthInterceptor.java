package com.itmo.mrdvd.service.interceptors;

import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.AuthResponse;
import com.itmo.mrdvd.AuthServiceGrpc.AuthServiceImplBase;
import com.itmo.mrdvd.AuthStatus;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import io.grpc.Metadata;
import io.grpc.stub.StreamObserver;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TokenAuthInterceptor extends AuthInterceptor {
  private final AuthServiceImplBase authService;
  private final Mapper<Metadata, AuthID> idMapper;

  public TokenAuthInterceptor(AuthServiceImplBase authService, Mapper<Metadata, AuthID> idMapper) {
    this.authService = authService;
    this.idMapper = idMapper;
  }

  @Override
  protected boolean authCheck(Metadata headers) {
    Optional<AuthID> idOpt = idMapper.convert(headers);
    if (idOpt.isEmpty()) {
      return false;
    }
    AuthID id = idOpt.get();
    CompletableFuture<Boolean> authResult = new CompletableFuture<>();
    authService.check(
        id,
        new StreamObserver<AuthResponse>() {
          @Override
          public void onNext(AuthResponse response) {
            authResult.complete(response.getStatus().equals(AuthStatus.AUTHORIZED));
          }

          @Override
          public void onError(Throwable t) {
            authResult.complete(false);
          }

          @Override
          public void onCompleted() {}
        });
    try {
      return authResult.get(2, TimeUnit.SECONDS);
    } catch (Exception e) {
      return false;
    }
  }
}
