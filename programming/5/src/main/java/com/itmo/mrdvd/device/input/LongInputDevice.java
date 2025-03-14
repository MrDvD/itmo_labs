package com.itmo.mrdvd.device.input;

import java.util.Optional;

public interface LongInputDevice extends InputDevice {
   public Optional<Long> readLong();
}
