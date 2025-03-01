package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;

public class SaveCommand implements Command {
  private final TicketCollection collection;
  private final Serializer<TicketCollection> serial;
  private final OutputDevice log;
  private final FileIO file;

  public SaveCommand(
      TicketCollection collect,
      Serializer<TicketCollection> serial,
      FileIO file,
      OutputDevice log) {
    this.collection = collect;
    this.serial = serial;
    this.file = file;
    this.log = log;
  }

  @Override
  public void execute(String[] params) {
    String result = serial.serialize(collection);
    if (result == null) {
      log.writeln("[ERROR] Ошибка сериализации коллекции.");
      return;
    }
    int code = file.openOut();
    while (code == -1) {
      if (file.createFile() != 0) {
        log.writeln("[ERROR] Ошибка создания файла с коллекцией.");
        return;
      }
      code = file.openOut();
    }
    if (code != 0) {
      switch (code) {
        case -3 -> log.writeln("[ERROR] Не указан путь к файлу для сохранения коллекции.");
        default -> log.writeln("[ERROR] Ошибка доступа к файлу для сохранения коллекции.");
      }
      return;
    }
    if (file.writeln(result) == 0) {
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
