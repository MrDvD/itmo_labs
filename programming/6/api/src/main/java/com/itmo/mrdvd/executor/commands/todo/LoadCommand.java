package com.itmo.mrdvd.executor.commands.todo;

import com.itmo.mrdvd.builder.validators.Validator;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.shell.ProxyShell;
import java.io.IOException;
import java.util.Optional;

public class LoadCommand<T extends HavingId, U> implements Command {
  private final InputDevice in;
  private final Collection<T, U> collection;
  private final Validator<T> validator;
  private final Deserializer<Collection<T, U>> deserial;
  private final ProxyShell<?, ?> shell;

  public LoadCommand(
      InputDevice in,
      Collection<T, U> collection,
      Validator<T> validator,
      Deserializer<Collection<T, U>> deserial) {
    this(in, collection, validator, deserial, null);
  }

  public LoadCommand(
      InputDevice in,
      Collection<T, U> collection,
      Validator<T> validator,
      Deserializer<Collection<T, U>> deserial,
      ProxyShell<?, ?> shell) {
    this.in = in;
    this.collection = collection;
    this.validator = validator;
    this.deserial = deserial;
    this.shell = shell;
  }

  @Override
  public LoadCommand<T, U> setShell(ProxyShell<?, ?> shell) {
    return new LoadCommand<>(in, collection, validator, deserial, shell);
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
    IOStatus code = in.openIn();
    if (code.equals(IOStatus.FAILURE)) {
      getShell().get().getOut().writeln("[ERROR] Не удалось обратиться к файлу с коллекцией.");
      return;
    }
    Optional<String> fileContent = Optional.empty();
    try {
      fileContent = in.readAll();
    } catch (IOException e) {
    }
    in.closeIn();
    if (fileContent.isEmpty()) {
      getShell().get().getOut().writeln("[ERROR] Ошибка чтения файла с коллекцией.");
      return;
    }
    Optional<Collection<T, U>> loaded = deserial.deserialize(fileContent.get());
    if (loaded.isPresent()) {
      collection.clear();
      for (T t : loaded.get()) {
        if (collection.add(t, validator).isEmpty()) {
          getShell()
              .get()
              .getOut()
              .writeln(
                  String.format(
                      "[ERROR] Невозможно добавить элемент № %d в коллекцию: не прошел валидацию.",
                      t.getId()));
        }
      }
      collection.setMetadata(loaded.get().getMetadata());
      getShell().get().getOut().writeln("[INFO] Коллекция успешно считана из файла.");
    } else {
      getShell()
          .get()
          .getOut()
          .writeln("[ERROR] Невозможно конвертировать структуру файла с коллекцией.");
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
