package com.itmo.mrdvd.proxy.mappers;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;

/**
 * A strange hacky solution, but at least it works.
 */
public class AuthQueryWrapper implements Mapper<ServiceQuery, ServiceQuery> {
  private final Mapper<Map<String, String>, LoginPasswordPair> authMapper;

  public AuthQueryWrapper(Mapper<Map<String, String>, LoginPasswordPair> authMapper) {
    this.authMapper = authMapper;
  }

  @Override
  public Optional<ServiceQuery> convert(ServiceQuery p) {
    if (p.getArgs().isEmpty()) {
      return Optional.empty();
    }
    Optional<LoginPasswordPair> pair = Optional.empty();
    try {
      pair = this.authMapper.convert((Map) p.getArgs().get(0));
    } catch (ClassCastException e) {
      return Optional.empty();
    }
    if (pair.isPresent()) {
      return Optional.of(ServiceQuery.of(p.getName(), Stream.concat(Stream.of(pair.get()), p.getArgs().stream().skip(1)).toList()));
    }
    return Optional.empty();
  }
}
