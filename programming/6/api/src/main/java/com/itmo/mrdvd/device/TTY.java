package com.itmo.mrdvd.device;

import com.itmo.mrdvd.device.input.DataInputDevice;

/**
 * Wraps the input and output devices for convenience.
 *
 * <p>In contrast to InteractiveInputDevice, this class allows different input and output devices.
 */
public abstract class TTY {
  protected DataInputDevice in;
  protected OutputDevice out;

  public TTY(DataInputDevice in, OutputDevice out) {
    this.in = in;
    this.out = out;
  }

  public TTY setIn(DataInputDevice in) {
    this.in = in;
    return this;
  }

  public TTY setOut(OutputDevice out) {
    this.out = out;
    return this;
  }

  public DataInputDevice getIn() {
    return this.in;
  }

  public OutputDevice getOut() {
    return this.out;
  }
}
