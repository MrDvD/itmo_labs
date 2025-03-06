package com.itmo.mrdvd.shell;

import java.util.Map;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;

public interface Shell {
  public InteractiveInputDevice getInput();

  public OutputDevice getOutput();

  public Map<String, Command> getCommands();

  public int addCommand(Command cmd);

  public Command getCommand(String str);

  public int getStackSize();

  public void setStackSize(int size);

  public int processCommandLine(String cmd);

  public void open();

  public void close();
}
