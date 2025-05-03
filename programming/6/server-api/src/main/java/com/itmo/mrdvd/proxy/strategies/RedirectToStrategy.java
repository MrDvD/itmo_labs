package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.response.Response;

public class RedirectToStrategy implements ProxyStrategy {
  private final Proxy other;

  public RedirectToStrategy(Proxy other) {
    this.other = other;
  }

  @Override
  public Response make(Query q) {
    if (this.other == null) {
      throw new IllegalStateException("Не обнаружен прокси для перенаправления.");
    }
    return this.other.processQuery(q);
  }
}