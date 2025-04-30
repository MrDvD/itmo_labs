package com.itmo.mrdvd.device;

import com.itmo.mrdvd.device.input.DataInputDevice;

/**
 * Wraps the input and output devices for convenience & names such pair.
 *
 * <p>In contrast to InteractiveInputDevice, this class allows different input and output devices.
 */
public abstract class TTY {
  protected String name;
  protected DataInputDevice in;
  protected OutputDevice out;

  public TTY(String name, DataInputDevice in, OutputDevice out) {
    this.name = name;
    this.in = in;
    this.out = out;
  }

  public TTY setName(String name) {
    this.name = name;
    return this;
  }

  public TTY setIn(DataInputDevice in) {
    this.in = in;
    return this;
  }

  public TTY setOut(OutputDevice out) {
    this.out = out;
    return this;
  }

  public String getName() {
    return this.name;
  }

  public DataInputDevice getIn() {
    return this.in;
  }

  public OutputDevice getOut() {
    return this.out;
  }
}
