package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.Service;
import com.itmo.mrdvd.service.executor.Command;
import java.util.List;

public class ExitCommand implements Command<Void> {
  @Override
  public Void execute(List<Object> params) throws IllegalArgumentException {
    if (params.isEmpty()) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    try {
      Service s = (Service) params.get(0);
      s.stop();
    } catch (ClassCastException e) {
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
