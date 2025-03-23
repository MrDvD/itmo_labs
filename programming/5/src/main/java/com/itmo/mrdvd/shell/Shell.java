package com.itmo.mrdvd.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.command.marker.Command;
import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.device.input.InputDevice;

public abstract class Shell<T,S,U extends DataInputDevice> implements Iterable<Command> {
   protected final T commands;
   protected final S preExecute;
   protected LinkedInput<U> linkedIn;
   protected final OutputDevice out;
   protected int stackSize;
   protected final List<StackRecord<?, ?>> stack;

   public Shell(OutputDevice out, LinkedInput<U> linkedIn, T commands, S preExecute) {
      this(out, linkedIn, commands, preExecute, new ArrayList<>(), 256);
   }

   public Shell(OutputDevice out, LinkedInput<U> linkedIn, T commands, S preExecute, List<StackRecord<?, ?>> stack, int stackSize) {
      this.linkedIn = linkedIn;
      this.out = out;
      this.commands = commands;
      this.preExecute = preExecute;
      this.stackSize = stackSize;
      this.stack = stack;
   }
  public InputDevice getIn() {
   return this.linkedIn.input();
  }

  public void setIn(U in) {
   this.linkedIn = this.linkedIn.update(in);
  }

  public void revertIn() {
   if (this.linkedIn.prev().isPresent()) {
      this.linkedIn = this.linkedIn.prev().get();
   } 
  }

  public OutputDevice getOut() {
   return this.out;
  }

  public V getStack() {
    return this.stack;
  }

  public void getStackCapacity(int size) {
    this.stackSize = size;
  }

  public abstract int getStackSize();

  public abstract void push(Object obj);

  public int getStackCapacity() {
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

  public Optional<Command> processCommandLine() {
   Optional<String> cmdName = getIn().readToken();
   if (cmdName.isEmpty()) {
      return Optional.empty();
   }
   Optional<Command> cmd = getCommand(cmdName.get());
   if (cmd.isPresent()) {
      if (!(cmd.get() instanceof CommandHasParams)) {
         getIn().skipLine();
      }
     cmd.get().execute();
     return cmd;
   }
   return Optional.empty();
 }

  public abstract void open();

  public abstract void close();
}
