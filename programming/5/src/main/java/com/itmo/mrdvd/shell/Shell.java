package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import java.util.Map;

public interface Shell {
  public InteractiveInputDevice getInput();

  public OutputDevice getOutput();

  public abstract Map<String, Command> getCommands();

  public abstract int getStackSize();

  public abstract void setStackSize(int size);

  public abstract FileDescriptor createFd();

  public abstract int processCommandLine(String cmd);

  public abstract void open();

  public abstract void close();
}
