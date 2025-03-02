package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import java.util.Map;

public interface Shell {
  public InteractiveInputDevice getInput();

  public OutputDevice getOutput();

  public Map<String, Command> getCommands();

  public int addCommand(Command cmd);

  public Command getCommand(String str);

  public int getStackSize();

  public void setStackSize(int size);

  public FileDescriptor createFd();

  public int processCommandLine(String cmd);

  public void open();

  public void close();
}
