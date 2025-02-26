package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.InputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import java.util.Map;

public abstract class Shell {
  private InputDevice in;
  private OutputDevice out;

  public Shell(InputDevice in, OutputDevice out) {
    this.in = in;
    this.out = out;
  }

  public InputDevice getInput() {
    return in;
  }

  public OutputDevice getOutput() {
    return out;
  }

  public abstract Map<String, Command> getCommands();

  public abstract void open();

  public abstract void close();
}
