package com.itmo.mrdvd.device.input;

import com.itmo.mrdvd.device.IOStatus;
import java.util.Optional;

public interface InputDevice {
  public boolean hasNext();

  public Optional<String> read() throws NullPointerException;

  public Optional<String> readToken() throws NullPointerException;

  public Optional<String> readAll() throws NullPointerException;

  public void skipLine() throws NullPointerException;

  public IOStatus openIn() throws NullPointerException;

  public IOStatus closeIn();
}
