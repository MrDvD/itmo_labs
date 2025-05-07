package com.itmo.mrdvd.builder.interactors;

import com.itmo.mrdvd.functionals.ExConsumer;
import com.itmo.mrdvd.functionals.ExSupplier;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public abstract class Interactor<U> {
  protected final ExSupplier<Optional<U>, IOException> in;
  protected final ExConsumer<String, IOException> out;
  private final String attributeName;
  private final Optional<List<String>> options;
  private final String error;
  private final Optional<String> comment;

  public Interactor(
      String attributeName,
      ExSupplier<Optional<U>, IOException> in,
      ExConsumer<String, IOException> out,
      String error,
      List<String> options,
      String comment) {
    this.attributeName = attributeName;
    this.in = in;
    this.out = out;
    this.error = error;
    this.options = Optional.ofNullable(options);
    this.comment = Optional.ofNullable(comment);
  }

  public String attributeName() {
    return this.attributeName;
  }

  /*
   * Asks the user for the input.
   */
  public abstract Optional<U> ask() throws IOException;

  public Optional<List<String>> options() {
    return this.options;
  }

  public Optional<String> comment() {
    return this.comment;
  }

  public void showError() throws IOException {
    this.out.accept(this.error);
  }
}
