package com.itmo.mrdvd.service.interceptors;

import io.grpc.Context;
import io.grpc.Metadata;

public class KeyFillContextInterceptor extends FillContextInterceptor {
  public KeyFillContextInterceptor(Context.Key<Object> key) {
    super(
        (t) -> {
          Metadata.Key<?> metaKey =
              Metadata.Key.of(key.toString(), Metadata.ASCII_STRING_MARSHALLER);
          if (!t.containsKey(metaKey)) {
            return Context.current();
          }
          Object tokenValue = t.get(metaKey);
          return Context.current().withValue(key, tokenValue);
        });
  }
}
