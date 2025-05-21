package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ObjectWrapperStrategy<T> implements ProxyStrategy {
  private final Mapper<Map<String, Object>, T> mapper;
  private final ProxyStrategy prev;

  public ObjectWrapperStrategy(Mapper<Map<String, Object>, T> mapper) {
    this(mapper, null);
  }

  public ObjectWrapperStrategy(Mapper<Map<String, Object>, T> mapper, ProxyStrategy prev) {
    this.mapper = mapper;
    this.prev = prev;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    if (this.prev != null) {
      Optional<ServiceQuery> newQ = this.prev.make(q);
      if (newQ.isPresent() && newQ.get().getName().equals(q.getName())) {
        q = newQ.get();
      } else {
        return newQ;
      }
    }
    if (q.getArgs().isEmpty()) {
      return Optional.empty();
    }
    Optional<T> obj = Optional.empty();
    try {
      obj = this.mapper.convert((Map) q.getArgs().get(0));
    } catch (ClassCastException e) {
      return Optional.empty();
    }
    if (obj.isPresent()) {
      return Optional.of(
          ServiceQuery.of(
              q.getName(),
              Stream.concat(Stream.of(obj.get()), q.getArgs().stream().skip(1)).toList()));
    }
    return Optional.empty();
  }
}
