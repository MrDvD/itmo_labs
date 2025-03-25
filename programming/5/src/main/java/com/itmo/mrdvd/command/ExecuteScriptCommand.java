package com.itmo.mrdvd.command;

import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.command.marker.ShellCommand;
import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.shell.Shell;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class ExecuteScriptCommand implements ShellCommand, CommandHasParams {
  private final Shell shell;
  private final DataFileDescriptor fd;
  private final Set<Path> usedPaths;

  public ExecuteScriptCommand(DataFileDescriptor fd, Set<Path> usedPaths) {
    this(fd, usedPaths, null);
  }

  public ExecuteScriptCommand(DataFileDescriptor fd, Set<Path> usedPaths, Shell shell) {
    this.fd = fd;
    this.usedPaths = usedPaths;
    this.shell = shell;
  }

  @Override
  public InputDevice getParamsInput() {
    return this.shell.getIn();
  }

  @Override
  public ExecuteScriptCommand setShell(Shell<?, ?> shell) {
    return new ExecuteScriptCommand(fd, usedPaths, shell);
  }

  @Override
  public Optional<Shell<?, ?>> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute() {
    if (shell == null) {
      throw new NullPointerException("Shell не может быть null.");
    }
    Optional<String> params = shell.getIn().readToken();
    shell.getIn().skipLine();
    DataFileDescriptor file = fd.duplicate();
    try {
      file.setPath(params.get());
    } catch (InvalidPathException | NoSuchElementException e) {
      shell.getOut().writeln("[ERROR] Неправильный формат ввода параметров команды.");
      return;
    }

    IOStatus code = file.openIn();
    if (code.equals(IOStatus.FAILURE)) {
      shell.getOut().writeln("[ERROR] Не удалось обратиться к файлу со скриптом.");
      return;
    }
    Optional<Path> path = file.getPath();
    if (path.isEmpty()) {
      shell.getOut().writeln("[ERROR] Неверный адрес к файлу со скриптом.");
      return;
    }
    if (usedPaths.contains(path.get())) {
      shell
          .getOut()
          .writeln(
              String.format(
                  "[WARN] Обнаружена петля, экстренное завершение исполнения скрипта \"%s\".",
                  path.get().getFileName().toString()));
      return;
    }
    usedPaths.add(path.get());
    file.openIn();
    shell.forkSubshell().setIn(file).open();
    usedPaths.remove(path.get());
  }

  @Override
  public String name() {
    return "execute_script";
  }

  @Override
  public String signature() {
    return name() + " file_name";
  }

  @Override
  public String description() {
    return "считать и исполнить скрипт из указанного файла";
  }
}
