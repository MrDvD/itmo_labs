package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.itmo.mrdvd.device.input.InputDevice;

public class UserInteractor<U> implements Interactor<U> {
  private final String attributeName;
  private final Function<InputDevice, Optional<U>> inMethod;
  private final Optional<List<String>> options;
  private final String error;
  private final Optional<String> comment;

  public UserInteractor(String attributeName, Function<InputDevice, Optional<U>> inMethod, String error) {
    this(attributeName, inMethod, error, null, null);
  }

  public UserInteractor(
      String attributeName, Function<InputDevice, Optional<U>> inMethod, String error, String comment) {
    this(attributeName, inMethod, error, null, comment);
  }

  public UserInteractor(
      String attributeName, Function<InputDevice, Optional<U>> inMethod, String error, List<String> options) {
    this(attributeName, inMethod, error, options, null);
  }

  public <X extends InputDevice> UserInteractor(
      String attributeName,
      Function<InputDevice, Optional<U>> inMethod,
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
  public <T extends InputDevice> Optional<U> get(T in) {
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