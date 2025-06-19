package com.itmo.mrdvd.builder.interactors;

import com.itmo.mrdvd.functionals.ExConsumer;
import com.itmo.mrdvd.functionals.ExSupplier;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserInteractor<U> extends Interactor<U> {
  public UserInteractor(
      String attributeName,
      ExSupplier<Optional<U>, IOException> in,
      ExConsumer<String, IOException> out,
      String error) {
    this(attributeName, in, out, error, null, null);
  }

  public UserInteractor(
      String attributeName,
      ExSupplier<Optional<U>, IOException> in,
      ExConsumer<String, IOException> out,
      String error,
      String comment) {
    this(attributeName, in, out, error, null, comment);
  }

  public UserInteractor(
      String attributeName,
      ExSupplier<Optional<U>, IOException> in,
      ExConsumer<String, IOException> out,
      String error,
      List<String> options) {
    this(attributeName, in, out, error, options, null);
  }

  public UserInteractor(
      String attributeName,
      ExSupplier<Optional<U>, IOException> in,
      ExConsumer<String, IOException> out,
      String error,
      List<String> options,
      String comment) {
    super(attributeName, in, out, error, options, comment);
  }

  @Override
  public Optional<U> ask() throws IOException {
    return ask(null);
  }

  @Override
  public Optional<U> ask(String context) throws IOException {
    if (this.in == null) {
      throw new IllegalStateException("Не передано устройство ввода.");
    }
    if (this.out == null) {
      throw new IllegalStateException("Не передано устройство вывода.");
    }
    String msg = "";
    if (options().isPresent()) {
      msg += String.format("Выберите поле \"%s\" из списка:\n", attributeName());
      for (int j = 0; j < options().get().size(); j++) {
        msg += String.format("* %s\n", options().get().get(j));
      }
      msg += "Ваш выбор";
    } else {
      msg += String.format("Введите поле \"%s\"", attributeName());
    }
    if (comment().isPresent()) {
      msg += String.format(" (%s)", comment().get());
    }
    if (context != null) {
      msg += String.format(" [%.15s]", context);
    }
    msg += ": ";
    this.out.accept(msg);
    return this.in.get();
  }
}
