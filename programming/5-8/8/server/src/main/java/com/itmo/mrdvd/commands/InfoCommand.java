package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.CollectionMeta;
import com.itmo.mrdvd.CollectionMetaValue;
import com.itmo.mrdvd.collection.AccessWorker;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InfoCommand implements Command<CollectionMeta> {
  private final AccessWorker<Map<String, Object>> metaCollection;

  public InfoCommand(AccessWorker<Map<String, Object>> metaCollection) {
    this.metaCollection = metaCollection;
  }

  @Override
  public CollectionMeta execute(List<Object> params) throws IllegalStateException {
    if (this.metaCollection == null) {
      throw new IllegalStateException("Не предоставлена коллекция для работы.");
    }
    Optional<Map<String, Object>> meta = this.metaCollection.get();
    if (meta.isPresent()) {
      CollectionMeta.Builder res = CollectionMeta.newBuilder();
      for (String key : meta.get().keySet()) {
        res.putFields(
            key,
            CollectionMetaValue.newBuilder()
                .setStringValue(meta.get().get(key).toString())
                .build());
      }
      return res.build();
    }
    return CollectionMeta.getDefaultInstance();
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
