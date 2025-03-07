package com.itmo.mrdvd.shell;

import java.util.Optional;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;

public interface Shell<T> {
  public InteractiveInputDevice getInput();

  public OutputDevice getOutput();

  public Iterable<Command> getCommands();

  public Optional<Command> addCommand(Command cmd);

  public Optional<Command> getCommand(String str);

  public int getStackSize();

  public void setStackSize(int size);

  public int processCommandLine(String cmd);

  public void open();

  public void close();
}
