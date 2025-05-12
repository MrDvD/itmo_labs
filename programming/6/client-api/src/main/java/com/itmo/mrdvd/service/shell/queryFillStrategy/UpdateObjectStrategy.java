package com.itmo.mrdvd.service.shell.queryFillStrategy;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.UpdateDTO;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.shell.AbstractShell;

public class UpdateObjectStrategy<T> implements QueryFillStrategy {
  private final AbstractShell shell;
  private final InteractiveBuilder<T> builder;
  private final QueryFillStrategy prev;

  public UpdateObjectStrategy(AbstractShell shell, InteractiveBuilder<T> builder) {
    this(shell, builder, null);
  }

  public UpdateObjectStrategy(
      AbstractShell shell, InteractiveBuilder<T> builder, QueryFillStrategy prev) {
    this.shell = shell;
    this.builder = builder;
    this.prev = prev;
  }

  @Override
  public ServiceQuery fillArgs(ServiceQuery q) throws IOException {
    if (prev != null) {
      q = prev.fillArgs(q);
    }
    Stream<Object> args = q.getArgs().stream();
    Optional<TTY> tty = this.shell.getTty();
    if (tty.isEmpty()) {
      throw new IllegalStateException("Не предоставлен TTY для чтения параметров.");
    }
    if (this.builder == null) {
      throw new IllegalStateException("Не предоставлен билдер для обновления объекта.");
    }
    Optional<Long> idx = tty.get().getIn().readLong();
    tty.get().getIn().skipLine();
    UpdateDTO<T> updateDTO = new UpdateDTO<>(); // Tight coupling with UpdateDTO, ikr
    if (idx.isPresent()) {
      updateDTO.setId(idx.get());
    }
    Optional<T> obj = this.builder.build();
    if (obj.isPresent()) {
      updateDTO.setObject(obj.get());
    }
    args = Stream.concat(args, Stream.of(updateDTO));
    q.setArgs(args.toList());
    return q;
  }
}
