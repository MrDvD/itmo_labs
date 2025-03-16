package com.itmo.mrdvd.shell;

import java.util.Optional;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InteractiveDataInputDevice;

public abstract class Shell<T,S,U extends InteractiveDataInputDevice> implements Iterable<Command> {
   private final T commands;
   private final S preExecute;
   private final U in;
   private final OutputDevice out;
   private final ShellParser parser;
   private int stackSize;

   public Shell(U in, OutputDevice out, T commands, S preExecute, ShellParser parser) {
      this.in = in;
      this.out = out;
      this.commands = commands;
      this.parser = parser;
      this.preExecute = preExecute;
      this.stackSize = 256;
   }
  public U getInput() {
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

  public Optional<Command> processCommandLine() {
   Optional<String> cmdName = getInput().readToken();
   if (cmdName.isEmpty()) {
      return Optional.empty();
   }
   Optional<Command> cmd = getCommand(cmdName.get());
   if (cmd.isPresent()) {
      if (!(cmd.get() instanceof CommandHasParams)) {
         getInput().skipLine();
      }
     cmd.get().execute();
     return cmd;
   }
   return Optional.empty();
 }

  public abstract void open();

  public abstract void close();
}
