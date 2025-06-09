package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.Meta;
import com.itmo.mrdvd.Value;
import com.itmo.mrdvd.collection.AccessWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InfoCommand implements Command<Meta> {
  private final AccessWorker<Map<String, Object>> metaCollection;

  public InfoCommand(AccessWorker<Map<String, Object>> metaCollection) {
    this.metaCollection = metaCollection;
  }

  @Override
  public Meta execute(List<Object> params) throws IllegalStateException {
    if (this.metaCollection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    Optional<Map<String, Object>> meta = this.metaCollection.get();
    if (meta.isPresent()) {
      Meta.Builder res = Meta.newBuilder();
      for (String key : meta.get().keySet()) {
        res.putFields(
            key, Value.newBuilder().setStringValue(meta.get().get(key).toString()).build());
      }
      return res.build();
    }
    return Meta.getDefaultInstance();
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
