package com.itmo.mrdvd.builder;

import com.itmo.mrdvd.device.input.InputDevice;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface Interactor<T, K extends InputDevice> {
  public String attributeName();

  public Optional<T> get(K in) throws IOException;

  public Optional<String> comment();

  public Optional<List<String>> options();

  public String error();
}
