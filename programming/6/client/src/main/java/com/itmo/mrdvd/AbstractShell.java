package com.itmo.mrdvd;

import java.util.Map;

import com.itmo.mrdvd.device.TTY;

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

  public void setTty(TTY tty) {
    this.tty = tty;
  }

  public TTY getTty() {
    return this.tty;
  }
}
