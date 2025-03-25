package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;

public interface Interactor<T> {
  public String attributeName();

  public Optional<T> get();

  public Optional<String> comment();

  public Optional<List<String>> options();

  public String error();
}
