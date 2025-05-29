package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.AccessWorker;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InfoCommand implements Command<String> {
  private final AccessWorker<Map<String, Object>> metaCollection;
  private final Mapper<? super Map<String, Object>, String> serializer;

  public InfoCommand(
      AccessWorker<Map<String, Object>> metaCollection,
      Mapper<? super Map<String, Object>, String> serializer) {
    this.metaCollection = metaCollection;
    this.serializer = serializer;
  }

  @Override
  public String execute(List<Object> params) throws IllegalStateException {
    if (this.metaCollection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    StringBuilder ans = new StringBuilder("# # # Метаданные коллекции # # #\n");
    Optional<Map<String, Object>> meta = this.metaCollection.get();
    if (meta.isPresent()) {
      this.serializer
          .convert(meta.get())
          .ifPresent(
              (t) -> {
                ans.append(t);
              });
    } else {
      ans.append("--- пусто ---");
    }
    return ans.toString();
  }

  @Override
  public String name() {
    return "info";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "вывести в стандартный поток вывода информацию о коллекции";
  }
}
