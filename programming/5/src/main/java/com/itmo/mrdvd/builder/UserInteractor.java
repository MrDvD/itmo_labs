package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.itmo.mrdvd.device.input.InputDevice;

public class UserInteractor<U,T extends InputDevice> implements Interactor<U> {
  private final String attributeName;
  private final Supplier<T> in;
  private final Function<T, Optional<U>> inMethod;
  private final Optional<List<String>> options;
  private final String error;
  private final Optional<String> comment;

  public UserInteractor(String attributeName, Supplier<T> in, Function<T, Optional<U>> inMethod, String error) {
    this(attributeName, in, inMethod, error, null, null);
  }

  public UserInteractor(
      String attributeName, Supplier<T> in, Function<T, Optional<U>> inMethod, String error, String comment) {
    this(attributeName, in, inMethod, error, null, comment);
  }

  public UserInteractor(
      String attributeName, Supplier<T> in, Function<T, Optional<U>> inMethod, String error, List<String> options) {
    this(attributeName, in, inMethod, error, options, null);
  }

  public UserInteractor(
      String attributeName,
      Supplier<T> in,
      Function<T, Optional<U>> inMethod,
      String error,
      List<String> options,
      String comment) {
    this.attributeName = attributeName;
    this.in = in;
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
  public Optional<U> get() {
    return this.inMethod.apply(this.in.get());
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