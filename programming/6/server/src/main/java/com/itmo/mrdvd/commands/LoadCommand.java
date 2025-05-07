package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.validators.Validator;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LoadCommand<T extends HavingId, U> implements Command<Void> {
  private final InputDevice in;
  private final Collection<T, U> collection;
  private final Validator<T> validator;
  private final Mapper<? extends Collection<T, U>, String> deserial;

  public LoadCommand(
      InputDevice in,
      Collection<T, U> collection,
      Validator<T> validator,
      Mapper<? extends Collection<T, U>, String> deserial,
      String params) {
    this.in = in;
    this.collection = collection;
    this.validator = validator;
    this.deserial = deserial;
  }

  @Override
  public Void execute(List<Object> params) throws RuntimeException {
    this.in.openIn();
    Optional<String> fileContent = Optional.empty();
    try {
      fileContent = in.readAll();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    in.closeIn();
    if (fileContent.isEmpty()) {
      throw new RuntimeException("Не удалось считать файл с коллекцией.");
    }
    Optional<? extends Collection<T, U>> loaded = this.deserial.unwrap(fileContent.get());
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
    return "load";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "загрузить коллекцию из файла";
  }
}
