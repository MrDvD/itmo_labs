package com.itmo.mrdvd.shell;

import java.util.Optional;

import com.itmo.mrdvd.device.input.InputDevice;

public interface LinkedInput<T extends InputDevice> {
   public <U extends LinkedInput<T>> U update(T nextInput);
   public <U extends LinkedInput<T>> Optional<U> prev();
   public T input();
}
