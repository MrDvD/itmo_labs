package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;

public class SaveCommand<T extends HavingId, U> implements Command {
  private final Collection<T, U> collection;
  private final Serializer<Collection<T, U>> serial;
  private final OutputDevice log;
  private final FileDescriptor file;

  public SaveCommand(
      Collection<T, U> collect,
      Serializer<Collection<T, U>> serial,
      FileDescriptor file,
      OutputDevice log) {
    this.collection = collect;
    this.serial = serial;
    this.file = file;
    this.log = log;
  }

  @Override
  public void execute() {
    Optional<String> result = serial.serialize(collection);
    if (result.isEmpty()) {
      log.writeln("[ERROR] Ошибка сериализации коллекции.");
      return;
    }
    IOStatus code = file.openOut();
    if (code.equals(IOStatus.FAILURE)) {
      log.writeln("[ERROR] Не удалось обратиться к файлу с коллекцией.");
      return;
    }
    if (file.writeln(result.get()).equals(IOStatus.SUCCESS)) {
      log.writeln("[INFO] Коллекция успешно записана в файл.");
      file.closeOut();
    } else {
      log.writeln("[ERROR] Произошла ошибка при записи в файл.");
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
