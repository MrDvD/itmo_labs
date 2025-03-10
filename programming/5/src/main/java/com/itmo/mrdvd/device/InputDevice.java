package com.itmo.mrdvd.device;

import java.util.Optional;

public interface InputDevice {
  public Optional<String> read() throws NullPointerException;

  public Optional<String> readAll();

  public IOStatus openIn() throws NullPointerException;

  public IOStatus closeIn();
}
