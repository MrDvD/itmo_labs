package com.itmo.mrdvd.device.input;

import java.util.Optional;

public interface InteractiveInputDevice extends InputDevice {
  public Optional<String> read(String message);

  public Optional<String> readToken(String message);

  public Optional<String> readAll(String message);
}
