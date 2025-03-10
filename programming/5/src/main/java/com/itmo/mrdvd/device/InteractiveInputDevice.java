package com.itmo.mrdvd.device;

import java.util.Optional;

public interface InteractiveInputDevice extends InputDevice {
  public Optional<String> read(String message);
}
