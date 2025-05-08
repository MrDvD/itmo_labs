package com.itmo.mrdvd.service.shell.query_fill_strategy;

import com.itmo.mrdvd.builder.updaters.InteractiveUpdater;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import java.io.IOException;
import java.util.stream.Stream;

public class UpdateObjectStrategy implements QueryFillStrategy {
  private final InteractiveUpdater<?> updater;
  private final QueryFillStrategy prev;

  public UpdateObjectStrategy(InteractiveUpdater<?> updater) {
    this(updater, null);
  }

  public UpdateObjectStrategy(InteractiveUpdater<?> updater, QueryFillStrategy prev) {
    this.updater = updater;
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Stream<Object> args = q.getArgs().stream();
    if (this.updater == null) {
      throw new IllegalStateException("Не предоставлен updater для обновления объекта.");
    }
    // Optional<?> obj = this.updater.update();
    // if (obj.isPresent()) {
    //   args = Stream.concat(args, Stream.of(obj.get()));
    // }
    q.setArgs(args.toList());
    return q;
  }
}
