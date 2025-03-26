package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.device.input.InputDevice;

public interface Interactor<T> {
  public String attributeName();

  public <U extends InputDevice> Optional<T> get(U in);

  public Optional<String> comment();

  public Optional<List<String>> options();

  public String error();
}
