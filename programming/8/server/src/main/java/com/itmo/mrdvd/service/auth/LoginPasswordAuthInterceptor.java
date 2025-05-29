package com.itmo.mrdvd.service.auth;

import com.itmo.mrdvd.Credentials;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import io.grpc.Metadata;
import java.util.List;

public class LoginPasswordAuthInterceptor extends AuthInterceptor {
  public LoginPasswordAuthInterceptor(AbstractExecutor exec) {
    this(
        exec,
        Metadata.Key.of("collection-login", Metadata.ASCII_STRING_MARSHALLER),
        Metadata.Key.of("collection-password", Metadata.ASCII_STRING_MARSHALLER));
  }

  public LoginPasswordAuthInterceptor(
      AbstractExecutor exec, Metadata.Key<?> loginKey, Metadata.Key<?> passwordKey) {
    super(
        (t) -> {
          if (!t.containsKey(loginKey) || !t.containsKey(passwordKey)) {
            return false;
          }
          try {
            Object login = t.get(loginKey);
            Object password = t.get(passwordKey);
            Object rawResult =
                exec.processCommand(
                    "login",
                    List.of(
                        Credentials.newBuilder()
                            .setLogin((String) login)
                            .setPassword((String) password)
                            .build()));
            return (Boolean) rawResult;
          } catch (ClassCastException e) {
            return false;
          }
        });
  }
}
