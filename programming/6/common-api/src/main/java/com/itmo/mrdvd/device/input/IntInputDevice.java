package com.itmo.mrdvd.device.input;

import java.io.IOException;
import java.util.Optional;

public interface IntInputDevice extends InputDevice {
  public Optional<Integer> readInt() throws IOException;
}
