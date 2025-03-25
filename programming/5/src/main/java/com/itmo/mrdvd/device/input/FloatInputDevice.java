package com.itmo.mrdvd.device.input;

import java.util.Optional;

public interface FloatInputDevice extends InputDevice {
  public Optional<Float> readFloat();
}
