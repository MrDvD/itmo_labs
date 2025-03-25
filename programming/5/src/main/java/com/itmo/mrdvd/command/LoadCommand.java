package com.itmo.mrdvd.command;

import com.itmo.mrdvd.builder.validators.Validator;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InputDevice;
import java.util.Optional;

public class LoadCommand<T extends HavingId, U> implements Command {
  private final InputDevice in;
  private final Collection<T, U> collection;
  private final Validator<T> validator;
  private final Deserializer<Collection<T, U>> deserial;
  private final OutputDevice out;

  public LoadCommand(
      InputDevice in,
      Collection<T, U> collection,
      Validator<T> validator,
      Deserializer<Collection<T, U>> deserial,
      OutputDevice out) {
    this.in = in;
    this.collection = collection;
    this.validator = validator;
    this.deserial = deserial;
    this.out = out;
  }

  @Override
  public void execute() {
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
    Optional<Collection<T, U>> loaded = deserial.deserialize(fileContent.get());
    if (loaded.isPresent()) {
      collection.clear();
      for (T t : loaded.get()) {
        if (collection.add(t, validator).isEmpty()) {
          out.writeln(
              String.format(
                  "[ERROR] Невозможно добавить элемент № %d в коллекцию: не прошел валидацию.",
                  t.getId()));
        }
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
