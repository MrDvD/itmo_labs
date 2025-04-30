package com.itmo.mrdvd.commands;

import java.util.List;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.service.executor.Command;

import com.itmo.mrdvd.proxy.mappers.Mapper;

public class SaveCommand<T extends HavingId, U> implements Command<Void> {
  private final Collection<T, U> collection;
  private final Mapper<? super Collection<T, U>, String> serial;
  private final FileDescriptor fd;

  public SaveCommand(
      Collection<T, U> collect, Mapper<? super Collection<T, U>, String> serial, FileDescriptor fd) {
    this.collection = collect;
    this.serial = serial;
    this.fd = fd;
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
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    String path = null;
    try {
      path = (String) params.get(0);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать путь к файлу.");
    }
    FileDescriptor file = this.fd.duplicate();
    file.setPath(path);
    file.openOut();
    file.writeln(this.serial.wrap(collection));
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
