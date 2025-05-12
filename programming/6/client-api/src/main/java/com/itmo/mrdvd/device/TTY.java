package com.itmo.mrdvd.device;

import com.itmo.mrdvd.device.input.DataInputDevice;

/**
 * Wraps the input and output devices for convenience & names such pair.
 *
 * <p>In contrast to InteractiveInputDevice, this class allows different input and output devices.
 */
public abstract class TTY {
  protected final String name;
  protected final DataInputDevice in;
  protected final OutputDevice out;

  public TTY(String name, DataInputDevice in, OutputDevice out) {
    this.name = name;
    this.in = in;
    this.out = out;
  }

  public abstract TTY setName(String name);

  public abstract TTY setIn(DataInputDevice in);

  public abstract TTY setOut(OutputDevice out);

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
