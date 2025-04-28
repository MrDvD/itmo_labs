package com.itmo.mrdvd;

import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.Service;

public abstract class AbstractShell implements Service {
  private final Map<String, Object> requestArgs;
  private TTY tty;

  public AbstractShell(TTY tty, Map<String, Object> args) {
    this.tty = tty;
    this.requestArgs = args;
  }

  /**
   * Automatically sets the args into the DtoRequest object with the given name.
   * 
   * The relevance of args is up to the implementation of the class.
   */
  public void setArg(String name, Object args) {
    this.requestArgs.put(name, args);
  }

  /**
   * Returns the args of the DtoRequest object with the given name.
   */
  public Optional<Object> getArg(String name) {
    return Optional.ofNullable(this.requestArgs.get(name));
  }

  public void setTty(TTY tty) {
    this.tty = tty;
  }

  public TTY getTty() {
    return this.tty;
  }
}
