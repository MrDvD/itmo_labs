package com.itmo.mrdvd.service.interceptors;

import io.grpc.Context;
import io.grpc.Metadata;

public class KeyFillContextInterceptor extends FillContextInterceptor {
  private final Context.Key<Object> key;

  public KeyFillContextInterceptor(Context.Key<Object> key) {
    this.key = key;
  }

  @Override
  public Context fillContext(Metadata meta) {
    Metadata.Key<?> metaKey =
        Metadata.Key.of(this.key.toString(), Metadata.ASCII_STRING_MARSHALLER);
    if (!meta.containsKey(metaKey)) {
      return Context.current();
    }
    Object tokenValue = meta.get(metaKey);
    return Context.current().withValue(this.key, tokenValue);
  }
}
