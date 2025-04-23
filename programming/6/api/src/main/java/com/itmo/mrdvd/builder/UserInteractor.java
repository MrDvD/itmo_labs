package com.itmo.mrdvd.builder;

import com.itmo.mrdvd.builder.functionals.ExFunction;
import com.itmo.mrdvd.device.input.InputDevice;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserInteractor<U, K extends InputDevice> implements Interactor<U, K> {
  private final String attributeName;
  private final ExFunction<K, Optional<U>, IOException> inMethod;
  private final Optional<List<String>> options;
  private final String error;
  private final Optional<String> comment;

  public UserInteractor(
      String attributeName, ExFunction<K, Optional<U>, IOException> inMethod, String error) {
    this(attributeName, inMethod, error, null, null);
  }

  public UserInteractor(
      String attributeName,
      ExFunction<K, Optional<U>, IOException> inMethod,
      String error,
      String comment) {
    this(attributeName, inMethod, error, null, comment);
  }

  public UserInteractor(
      String attributeName,
      ExFunction<K, Optional<U>, IOException> inMethod,
      String error,
      List<String> options) {
    this(attributeName, inMethod, error, options, null);
  }

  public UserInteractor(
      String attributeName,
      ExFunction<K, Optional<U>, IOException> inMethod,
      String error,
      List<String> options,
      String comment) {
    this.attributeName = attributeName;
    this.inMethod = inMethod;
    this.error = error;
    this.options = Optional.ofNullable(options);
    this.comment = Optional.ofNullable(comment);
  }

  @Override
  public String attributeName() {
    return this.attributeName;
  }

  @Override
  public Optional<U> get(K in) throws IOException {
    return this.inMethod.apply(in);
  }

  @Override
  public Optional<List<String>> options() {
    return this.options;
  }

  @Override
  public Optional<String> comment() {
    return this.comment;
  }

  @Override
  public String error() {
    return this.error;
  }
}
