package com.itmo.mrdvd.device.input;

import com.itmo.mrdvd.device.IOStatus;
import java.io.IOException;
import java.util.Optional;

public interface InputDevice {
  public boolean hasNext();

  public Optional<String> read() throws NullPointerException, IOException;

  public Optional<String> readToken() throws NullPointerException, IOException;

  public Optional<String> readAll() throws NullPointerException, IOException;

  public void skipLine() throws NullPointerException;

  public IOStatus openIn() throws NullPointerException;

  public IOStatus closeIn();
}
