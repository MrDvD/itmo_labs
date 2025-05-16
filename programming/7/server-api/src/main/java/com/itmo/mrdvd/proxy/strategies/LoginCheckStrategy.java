package com.itmo.mrdvd.proxy.strategies;

import java.util.Optional;

import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;

public class LoginCheckStrategy implements ProxyStrategy {
  private final ProxyStrategy next;

  public LoginCheckStrategy(ProxyStrategy next) {
    this.next = next;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    q.
    // Implement the logic to check if the user is logged in
    // If not, return error ServiceQuery
    // Otherwise, return the ServiceQuery object
    return q;
  }
}
