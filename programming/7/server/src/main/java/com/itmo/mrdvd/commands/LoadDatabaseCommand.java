package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LoadDatabaseCommand<T extends HavingId, U extends java.util.Collection<? extends T>> implements Command<Void> {
  private final FileDescriptor in;
  private final Collection<T, U> collection;
  private final Validator<T> validator;
  private final Mapper<String, ? extends Collection<T, U>> deserial;
  private final String path;

  public LoadDatabaseCommand(
      FileDescriptor in,
      Collection<T, U> collection,
      Validator<T> validator,
      Mapper<String, ? extends Collection<T, U>> deserial,
      String path) {
    this.in = in;
    this.collection = collection;
    this.validator = validator;
    this.deserial = deserial;
    this.path = path;
  }

  @Override
  public Void execute(List<Object> params) throws RuntimeException {
    this.in.setPath(path);
    this.in.openIn();
    Optional<String> fileContent = Optional.empty();
    try {
      fileContent = in.readAll();
    // } catch (IOException e) {
    //   throw new RuntimeException(e);
    // }
    in.closeIn();
    if (fileContent.isEmpty()) {
      throw new RuntimeException("Не удалось считать файл с коллекцией.");
    }
    Optional<? extends Collection<T, U>> loaded = this.deserial.convert(fileContent.get());
    if (loaded.isEmpty()) {
      throw new RuntimeException("Не удалось конвертировать структуру файла с коллекцией.");
    }
    collection.clear();
    for (T t : loaded.get()) {
      if (collection.add(t, validator).isEmpty()) {
        throw new RuntimeException(String.format("Элемент № %d не прошёл валидацию.", t.getId()));
      }
    }
    collection.setMetadata(loaded.get().getMetadata());
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
