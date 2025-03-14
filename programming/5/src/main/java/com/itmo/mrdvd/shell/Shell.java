package com.itmo.mrdvd.shell;

import java.util.Optional;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;

public abstract class Shell<T,S> implements Iterable<Command> {
   private final T commands;
   private final S preExecute;
   private final InteractiveInputDevice in;
   private final OutputDevice out;
   private final ShellParser parser;
   private int stackSize;

   public Shell(InteractiveInputDevice in, OutputDevice out, T commands, S preExecute, ShellParser parser) {
      this.in = in;
      this.out = out;
      this.commands = commands;
      this.parser = parser;
      this.preExecute = preExecute;
      this.stackSize = 256;
   }
  public InteractiveInputDevice getInput() {
   return this.in;
  }

  public OutputDevice getOutput() {
   return this.out;
  }

  public void setStackSize(int size) {
    this.stackSize = size;
  }

  public int getStackSize() {
    return this.stackSize;
  }

  public T getCommands() {
   return this.commands;
  }

  public S getPreExecute() {
   return this.preExecute;
  }

  public abstract Optional<Command> addCommand(Command cmd);

  public abstract Optional<Command> getCommand(String str);

  public ShellParser getParser() {
   return this.parser;
  }

  public ProcessingStatus processCommandLine(String line) {
   Optional<ShellQuery> rawCmd = getParser().parse(line);
   if (rawCmd.isEmpty()) {
     return ProcessingStatus.PARSE_FAILURE;
   }
   Optional<Command> cmd = getCommand(rawCmd.get().cmd());
   if (cmd.isPresent()) {
     cmd.get().execute(rawCmd.get().params());
     return ProcessingStatus.SUCCESS;
   }
   return ProcessingStatus.CMD_NOT_FOUND;
 }

  public abstract void open();

  public abstract void close();
}
