package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import java.util.Map;

public abstract class Shell {
  private final InteractiveInputDevice in;
  private final OutputDevice out;

  public Shell(InteractiveInputDevice in, OutputDevice out) {
    this.in = in;
    this.out = out;
  }

  public InteractiveInputDevice getInput() {
    return in;
  }

  public OutputDevice getOutput() {
    return out;
  }

  public abstract Map<String, Command> getCommands();

  public abstract void open();

  public abstract void close();
}
