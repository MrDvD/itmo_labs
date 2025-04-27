package com.itmo.mrdvd.device;

import com.itmo.mrdvd.device.input.DataInputDevice;

public class DefaultTTY extends TTY {
  public DefaultTTY(DataInputDevice in, OutputDevice out) {
    super(in, out);
  }
}
