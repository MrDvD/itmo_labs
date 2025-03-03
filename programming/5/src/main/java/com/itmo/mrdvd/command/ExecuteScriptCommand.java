package com.itmo.mrdvd.command;

import com.itmo.mrdvd.device.FileDescriptor;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.shell.Shell;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScriptCommand implements Command, ShellCommand {
  private Shell shell;
  private final OutputDevice log;
  private final Set<String> stack;

  public ExecuteScriptCommand(OutputDevice log) {
    this.log = log;
    this.stack = new HashSet<>();
  }

  public int validateParams(String[] params) {
    if (params.length != 1) {
      return -1;
    }
    return 0;
  }

  @Override
  public void setShell(Shell shell) {
    this.shell = shell;
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
    FileDescriptor file = shell.createFd();
    file.setPath(params[0]);
    int code = file.openIn();
    if (code != 0) {
      switch (code) {
        case -1 -> log.writeln("[ERROR] Файла со скриптом не существует, считывать нечего.");
        default -> log.writeln("[ERROR] Ошибка доступа к файлу со скриптом.");
      }
      return;
    }
    if (stack.contains(file.getPath())) {
      log.writeln(
          String.format(
              "[WARN] Обнаружена петля, экстренное завершение исполнения скрипта \"%s\".",
              file.getName()));
      return;
    }
    stack.add(file.getPath());
    String currLine = file.read();
    while (currLine != null) {
      shell.processCommandLine(currLine);
      currLine = file.read();
    }
    stack.remove(file.getPath());
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
