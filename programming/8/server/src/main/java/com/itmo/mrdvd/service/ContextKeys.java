package com.itmo.mrdvd.service;

import io.grpc.Context;

public enum ContextKeys {
  TOKEN("collect-token");

  private final Context.Key<Object> key;

  ContextKeys(String name) {
    this.key = Context.key(name);
  }

  public Context.Key<Object> getKey() {
    return key;
  }
}
