package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import java.io.IOException;
import java.util.Optional;

public abstract class Shell<T, S> implements Iterable<Command> {
  protected final T commands;
  protected final Optional<S> preExecute;
  protected DataInputDevice in;
  protected final OutputDevice out;

  public Shell(DataInputDevice in, OutputDevice out, T commands, S preExecute) {
    this.in = in;
    this.out = out;
    this.commands = commands;
    this.preExecute = Optional.ofNullable(preExecute);
  }

  public DataInputDevice getIn() {
    return this.in;
  }

  public OutputDevice getOut() {
    return this.out;
  }

  public T getCommands() {
    return this.commands;
  }

  public Optional<S> getPreExecute() {
    return this.preExecute;
  }

  public abstract Optional<Command> addCommand(Command cmd);

  public abstract Optional<Command> getCommand(String str);

  public Optional<Command> processCommandLine() throws IOException {
    Optional<String> cmdName = getIn().readToken();
    if (cmdName.isEmpty()) {
      getIn().skipLine();
      return Optional.empty();
    }
    Optional<Command> cmd = getCommand(cmdName.get());
    if (cmd.isPresent()) {
      if (!cmd.get().hasParams()) {
        getIn().skipLine();
      }
      cmd.get().execute();
      return cmd;
    }
    return Optional.empty();
  }

  public abstract Shell<T, S> forkSubshell(DataInputDevice in, OutputDevice out);

  public abstract void open();

  public abstract void close();
}
