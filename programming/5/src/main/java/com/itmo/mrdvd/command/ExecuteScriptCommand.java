package com.itmo.mrdvd.command;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.IOStatus;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.shell.Shell;

public class ExecuteScriptCommand implements Command, ShellInfo {
  private Shell<?, ?, ?> shell;
  private final OutputDevice log;
  private final Set<String> stack;
  private final FileDescriptor fd;

  public ExecuteScriptCommand(OutputDevice log, FileDescriptor fd) {
    this.log = log;
    this.stack = new HashSet<>();
    this.fd = fd;
  }

  public int validateParams(String[] params) {
    if (params.length != 1) {
      return -1;
    }
    return 0;
  }

  @Override
  public void setShell(Shell<?, ?, ?> shell) {
    this.shell = shell;
  }

  @Override
  public Optional<Shell<?, ?, ?>> getShell() {
   return Optional.ofNullable(this.shell);
  }

  @Override
  public void execute(String[] params) {
    if (shell == null) {
      return;
    }
    if (validateParams(params) != 0) {
      log.writeln("[ERROR] Неправильный формат ввода параметров команды.");
      return;
    }
    if (shell.getStackSize() <= this.stack.size()) {
      log.writeln("[ERROR] Превышен размер стека: слишком большой уровень вложенности.");
      return;
    }
    FileDescriptor file = fd.duplicate();
    file.setPath(params[0]);
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
    if (stack.contains(path.get().toAbsolutePath().toString())) {
      log.writeln(
          String.format(
              "[WARN] Обнаружена петля, экстренное завершение исполнения скрипта \"%s\".",
              path.get().getFileName().toString()));
      return;
    }
    stack.add(path.get().toAbsolutePath().toString());
    Optional<String> currLine = file.read();
    while (currLine.isPresent()) {
      shell.processCommandLine(currLine.get());
      currLine = file.read();
    }
    stack.remove(path.get().toAbsolutePath().toString());
    file.closeIn();
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
