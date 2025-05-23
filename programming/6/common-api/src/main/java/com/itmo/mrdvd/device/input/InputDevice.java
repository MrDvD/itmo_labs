package com.itmo.mrdvd.device.input;

import java.io.IOException;
import java.util.Optional;

public interface InputDevice {
  public boolean hasNext();

  public Optional<String> read() throws NullPointerException, IOException;

  public Optional<String> readToken() throws NullPointerException, IOException;

  public Optional<String> readAll() throws NullPointerException, IOException;

  public void skipLine() throws NullPointerException;

  public void openIn() throws NullPointerException;

  public void closeIn();
}
