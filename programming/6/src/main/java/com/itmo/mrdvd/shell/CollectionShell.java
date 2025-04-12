package com.itmo.mrdvd.shell;

import com.itmo.mrdvd.collection.HavingId;
import com.itmo.mrdvd.command.Command;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CollectionShell<T extends HavingId>
    extends Shell<Map<String, Command>, List<Command>> {
  private boolean isOpen;

  public CollectionShell(DataInputDevice in, OutputDevice out) {
    this(in, out, new HashMap<>(), new ArrayList<>());
  }

  public CollectionShell(
      DataInputDevice in,
      OutputDevice out,
      Map<String, Command> commands,
      List<Command> preExecute) {
    super(in, out, commands, preExecute);
    this.isOpen = false;
  }

  public Optional<Command> addCommand(Command cmd, boolean preExec) {
    if (getCommands().containsKey(cmd.name())) {
      return Optional.empty();
    }
    cmd = cmd.setShell(this);
    if (getPreExecute().isPresent() && preExec) {
      getPreExecute().get().add(cmd);
    }
    getCommands().put(cmd.name(), cmd);
    return Optional.of(cmd);
  }

  @Override
  public Optional<Command> addCommand(Command cmd) {
    return addCommand(cmd, false);
  }

  @Override
  public void open() {
    if (getPreExecute().isPresent()) {
      for (Command cmd : getPreExecute().get()) {
        cmd.execute();
      }
    }
    this.isOpen = true;
    while (this.isOpen) {
      if (InteractiveInputDevice.class.isInstance(getIn())) {
        ((InteractiveInputDevice) getIn()).write("> ");
      }
      while (!this.in.hasNext()) {
        this.in.closeIn();
        this.isOpen = false;
        return;
      }
      Optional<Command> cmd = Optional.empty();
      try {
        cmd = processCommandLine();
      } catch (IOException e) {
        close();
      }
      if (cmd.isEmpty()) {
        getOut()
            .writeln(
                "[ERROR] Команда не найдена: введите 'help' для просмотра списка доступных команд.");
      }
    }
  }

  @Override
  public Optional<Command> getCommand(String line) {
    return Optional.ofNullable(getCommands().get(line));
  }

  @Override
  public void close() {
    this.isOpen = false;
  }

  @Override
  public Iterator<Command> iterator() {
    return getCommands().values().iterator();
  }

  @Override
  public DataInputDevice getIn() {
    return (DataInputDevice) this.in;
  }

  @Override
  public CollectionShell<T> forkSubshell(DataInputDevice in, OutputDevice out) {
    CollectionShell<T> subshell = new CollectionShell<>(in, out);
    for (Command cmd : this) {
      subshell.addCommand(cmd.setShell(subshell));
    }
    return subshell;
  }
}
