package com.itmo.mrdvd.device;

import com.itmo.mrdvd.device.input.DataInputDevice;

public class DefaultTTY extends TTY {
  public DefaultTTY(String name, DataInputDevice in, OutputDevice out) {
    super(name, in, out);
  }

  @Override
  public DefaultTTY setIn(DataInputDevice in) {
    return new DefaultTTY(getName(), in, getOut());
  }

  @Override
  public DefaultTTY setOut(OutputDevice out) {
    return new DefaultTTY(getName(), getIn(), out);
  }

  @Override
  public DefaultTTY setName(String name) {
    return new DefaultTTY(name, getIn(), getOut());
  }
}
