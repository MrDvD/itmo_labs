package com.itmo.mrdvd.device.input;

import java.io.IOException;
import java.util.Optional;

public interface FloatInputDevice extends InputDevice {
  public Optional<Float> readFloat() throws IOException;
}
