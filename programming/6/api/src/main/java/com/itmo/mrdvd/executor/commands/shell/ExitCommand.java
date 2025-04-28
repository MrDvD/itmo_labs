package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.service.Service;

public class ExitCommand implements Command<Void> {
  @Override
  public Void execute(Object params) throws IllegalArgumentException {
    if (params instanceof Service s) {
      s.stop();
    } else {
      throw new IllegalArgumentException("Не предоставлен сервис для завершения.");
    }
    return null;
  }

  @Override
  public String name() {
    return "exit";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "завершить интерпретатор (без сохранения состояния)";
  }
}
