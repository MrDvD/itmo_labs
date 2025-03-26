package com.itmo.mrdvd.device.input;

import java.io.IOException;
import java.util.Optional;

public interface LongInputDevice extends InputDevice {
  public Optional<Long> readLong() throws IOException;
}
