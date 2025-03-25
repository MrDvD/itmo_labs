package com.itmo.mrdvd.command;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.device.DataFileDescriptor;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.shell.Shell;

public class ExecuteScriptCommand implements Command {
  private final Shell<?, ?> shell;
  private final DataFileDescriptor fd;
  private final Set<Path> usedPaths;

  public ExecuteScriptCommand(DataFileDescriptor fd, Set<Path> usedPaths) {
    this(fd, usedPaths, null);
  }

  public ExecuteScriptCommand(DataFileDescriptor fd, Set<Path> usedPaths, Shell<?, ?> shell) {
    this.fd = fd;
    this.usedPaths = usedPaths;
    this.shell = shell;
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
    if (getShell().isEmpty()) {
      throw new NullPointerException("Shell не может быть null.");
    }
    Optional<String> params = getShell().get().getIn().readToken();
    getShell().get().getIn().skipLine();
    DataFileDescriptor file = fd.duplicate();
    try {
      file.setPath(params.get());
    } catch (InvalidPathException | NoSuchElementException e) {
      getShell().get().getOut().writeln("[ERROR] Неправильный формат ввода параметров команды.");
      return;
    }

    IOStatus code = file.openIn();
    if (code.equals(IOStatus.FAILURE)) {
      getShell().get().getOut().writeln("[ERROR] Не удалось обратиться к файлу со скриптом.");
      return;
    }
    Optional<Path> path = file.getPath();
    if (path.isEmpty()) {
      getShell().get().getOut().writeln("[ERROR] Неверный адрес к файлу со скриптом.");
      return;
    }
    if (usedPaths.contains(path.get())) {
      getShell().get()
          .getOut()
          .writeln(
              String.format(
                  "[WARN] Обнаружена петля, экстренное завершение исполнения скрипта \"%s\".",
                  path.get().getFileName().toString()));
      return;
    }
    usedPaths.add(path.get());
    file.openIn();
    getShell().get().forkSubshell().setIn(file).open();
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

  @Override
  public boolean hasParams() {
    return true;
  }
}
