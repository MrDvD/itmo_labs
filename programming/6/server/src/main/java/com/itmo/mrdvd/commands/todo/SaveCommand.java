package com.itmo.mrdvd.commands.todo;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.service.executor.Command;
import com.itmo.mrdvd.shell.ProxyShell;
import java.util.Optional;

public class SaveCommand<T extends HavingId, U> implements Command {
  private final Collection<T, U> collection;
  private final Serializer<Collection<T, U>> serial;
  private final ProxyShell<?, ?> shell;
  private final FileDescriptor file;

  public SaveCommand(
      Collection<T, U> collect, Serializer<Collection<T, U>> serial, FileDescriptor file) {
    this(collect, serial, file, null);
  }

  public SaveCommand(
      Collection<T, U> collect,
      Serializer<Collection<T, U>> serial,
      FileDescriptor file,
      ProxyShell<?, ?> shell) {
    this.collection = collect;
    this.serial = serial;
    this.file = file;
    this.shell = shell;
  }

  @Override
  public SaveCommand<T, U> setShell(ProxyShell<?, ?> shell) {
    return new SaveCommand<>(collection, serial, file, shell);
  }

  @Override
  public Optional<ProxyShell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() throws NullPointerException {
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    Optional<String> result = serial.serialize(collection);
    if (result.isEmpty()) {
      getShell().get().getOut().writeln("[ERROR] Ошибка сериализации коллекции.");
      return;
    }
    IOStatus code = file.openOut();
    if (code.equals(IOStatus.FAILURE)) {
      getShell().get().getOut().writeln("[ERROR] Не удалось обратиться к файлу с коллекцией.");
      return;
    }
    if (file.writeln(result.get()).equals(IOStatus.SUCCESS)) {
      getShell().get().getOut().writeln("[INFO] Коллекция успешно записана в файл.");
      file.closeOut();
    } else {
      getShell().get().getOut().writeln("[ERROR] Произошла ошибка при записи в файл.");
    }
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
