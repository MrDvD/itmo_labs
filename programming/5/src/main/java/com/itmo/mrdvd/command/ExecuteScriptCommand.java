package com.itmo.mrdvd.command;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.command.marker.CommandHasParams;
import com.itmo.mrdvd.command.marker.ShellCommand;
import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.shell.Shell;

public class ExecuteScriptCommand implements ShellCommand, CommandHasParams {
  private Shell shell;
  private InputDevice in;
  private final OutputDevice log;
  private final FileDescriptor fd;
  private final Set<Path> usedPaths;

  public ExecuteScriptCommand(InputDevice in, OutputDevice log, FileDescriptor fd, Set<Path> usedPaths) {
    this.in = in; 
    this.log = log;
    this.fd = fd;
    this.usedPaths = usedPaths;
  }

  @Override
  public InputDevice getParamsInput() {
   return this.in;
  }

  @Override
  public void setShell(Shell<?, ?> shell) {
    this.shell = shell;
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
    FileDescriptor file = fd.duplicate();
    try {
      file.setPath(params.get());
    } catch (InvalidPathException e) {
      log.writeln("[ERROR] Неправильный формат ввода параметров команды.");
      return;
    }
    
    IOStatus code = file.openIn();
    if (code.equals(IOStatus.FAILURE)) {
      log.writeln("[ERROR] Не удалось обратиться к файлу со скриптом.");
      return;
    }
    Optional<Path> path = file.getPath();
    if (path.isEmpty()) {
      log.writeln("[ERROR] Неверный адрес к файлу со скриптом.");
      return;
    }
    if (usedPaths.contains(path.get())) {
      log.writeln(
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
