package com.itmo.mrdvd.device.input;

import java.io.IOException;
import java.util.Optional;

public interface EnumInputDevice extends InputDevice {
  public <T extends Enum<T>> Optional<Enum<T>> readEnum(Class<T> cls) throws IOException;
}
