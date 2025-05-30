package com.itmo.mrdvd.service.shell.queryFillStrategy;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

public class ReadObjectStrategy implements QueryFillStrategy {
  private final InteractiveBuilder<?> builder;
  private final QueryFillStrategy prev;

  public ReadObjectStrategy(InteractiveBuilder<?> builder) {
    this(builder, null);
  }

  public ReadObjectStrategy(InteractiveBuilder<?> builder, QueryFillStrategy prev) {
    this.builder = builder;
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Stream<Object> args = q.getArgs().stream();
    if (this.builder == null) {
      throw new IllegalStateException("Не предоставлен билдер для построения объекта.");
    }
    Optional<?> obj = this.builder.build();
    if (obj.isPresent()) {
      args = Stream.concat(args, Stream.of(obj.get()));
    }
    return ServiceQuery.of(q.getName(), args.toList());
  }
}
