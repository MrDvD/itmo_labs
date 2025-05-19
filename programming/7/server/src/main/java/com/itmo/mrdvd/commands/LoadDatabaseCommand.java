package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.CrudWorker;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class LoadDatabaseCommand<T extends HavingId, U extends java.util.Collection<? extends T>>
    implements Command<Void> {
  private final CrudWorker<T, U> dbworker;
  private final Collection<T, U> collection;

  public LoadDatabaseCommand(CrudWorker<T, U> dbworker, Collection<T, U> collection) {
    this.dbworker = dbworker;
    this.collection = collection;
  }

  @Override
  public Void execute(List<Object> params) throws RuntimeException {
    if (this.dbworker == null) {
      throw new IllegalStateException("Не задана СУБД для работы.");
    }
    if (this.collection == null) {
      throw new IllegalStateException("Не задана коллекция для работы.");
    }
    collection.setCache(dbworker.getAll());
    // collection.setMetadata(loaded.get().getMetadata());
    return null;
  }

  @Override
  public String name() {
    return "load_db";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "загрузить коллекцию из базы данных";
  }
}
