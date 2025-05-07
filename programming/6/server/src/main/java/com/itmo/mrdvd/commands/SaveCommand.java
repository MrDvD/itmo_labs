package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class SaveCommand<T extends HavingId, U> implements Command<Void> {
  private final Collection<T, U> collection;
  private final Mapper<? super Collection<T, U>, String> serial;
  private final FileDescriptor fd;
  private final String path;

  public SaveCommand(
      Collection<T, U> collect,
      Mapper<? super Collection<T, U>, String> serial,
      FileDescriptor fd,
      String path) {
    this.collection = collect;
    this.serial = serial;
    this.fd = fd;
    this.path = path;
  }

  @Override
  public Void execute(List<Object> params) throws IllegalStateException {
    if (this.serial == null) {
      throw new IllegalStateException("Не задан сериализатор для коллекции.");
    }
    if (this.collection == null) {
      throw new IllegalStateException("Не задана коллекция для сериализации.");
    }
    if (this.fd == null) {
      throw new IllegalStateException("Не задан файловый дескриптор для записи коллекции.");
    }
    FileDescriptor file = this.fd.duplicate();
    if (file.setPath(this.path).isEmpty()) {
      throw new IllegalStateException("Не удалось распознать путь к файлу.");
    }
    file.openOut();
    file.writeln(this.serial.wrap(collection));
    file.closeOut();
    return null;
  }

  @Override
  public String name() {
    return "save";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "сохранить коллекцию в файл";
  }
}
