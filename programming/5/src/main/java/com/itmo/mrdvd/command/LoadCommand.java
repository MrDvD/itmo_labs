package com.itmo.mrdvd.command;

import java.util.Optional;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.InputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;

public class LoadCommand implements Command {
  private final InputDevice in;
  private final TicketCollection collection;
  private final Deserializer<TicketCollection> deserial;
  private final OutputDevice out;

  public LoadCommand(
      InputDevice in,
      TicketCollection collection,
      Deserializer<TicketCollection> deserial,
      OutputDevice out) {
    this.in = in;
    this.collection = collection;
    this.deserial = deserial;
    this.out = out;
  }

  @Override
  public void execute(String[] params) {
    IOStatus code = in.openIn();
    if (code.equals(IOStatus.FAILURE)) {
      out.writeln("[ERROR] Не удалось обратиться к файлу с коллекцией.");
      return;
    }
    Optional<String> fileContent = in.readAll();
    in.closeIn();
    if (fileContent.isEmpty()) {
      out.writeln("[ERROR] Ошибка чтения файла с коллекцией.");
      return;
    }
    Optional<TicketCollection> loaded = deserial.deserialize(fileContent.get());
    if (loaded.isPresent()) {
      collection.clear();
      for (Ticket t : loaded.get()) {
        collection.addRaw(t);
      }
      collection.setMetadata(loaded.get().getMetadata());
      out.writeln("[INFO] Коллекция успешно считана из файла.");
    } else {
      out.writeln("[ERROR] Невозможно конвертировать структуру файла с коллекцией.");
    }
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
