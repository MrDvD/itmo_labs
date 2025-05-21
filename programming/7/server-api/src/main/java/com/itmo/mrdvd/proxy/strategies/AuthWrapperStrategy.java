package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class AuthWrapperStrategy implements ProxyStrategy {
  private final Mapper<Map<String, String>, LoginPasswordPair> authMapper;

  public AuthWrapperStrategy(Mapper<Map<String, String>, LoginPasswordPair> authMapper) {
    this.authMapper = authMapper;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery p) {
    if (p.getArgs().isEmpty()) {
      return Optional.empty();
    }
    if (p.getArgs().get(0) instanceof LoginPasswordPair) {
      return Optional.of(p);
    }
    Optional<LoginPasswordPair> pair = Optional.empty();
    try {
      pair = this.authMapper.convert((Map) p.getArgs().get(0));
    } catch (ClassCastException e) {
      return Optional.empty();
    }
    if (pair.isPresent()) {
      return Optional.of(
          ServiceQuery.of(
              p.getName(),
              Stream.concat(Stream.of(pair.get()), p.getArgs().stream().skip(1)).toList()));
    }
    return Optional.empty();
  }
}
