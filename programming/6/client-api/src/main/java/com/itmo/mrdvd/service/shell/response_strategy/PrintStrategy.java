package com.itmo.mrdvd.service.shell.response_strategy;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import com.itmo.mrdvd.service.shell.AbstractShell;
import java.util.List;
import java.util.Optional;

public class PrintStrategy implements ShellResponseStrategy {
  private final AbstractShell shell;

  public PrintStrategy(AbstractShell shell) {
    this.shell = shell;
  }

  @Override
  public void make(ServiceQuery r) throws IllegalStateException {
    Optional<TTY> tty = this.shell.getTty();
    if (tty.isPresent()) {
      tty.get().getOut().writeln(String.format("[%s]: ", r.getName()));
      try {
        List<Object> lst = r.getArgs();
        if (!lst.isEmpty()) {
          tty.get().getOut().writeln((String) lst.get(0));
        }
      } catch (ClassCastException e) {
        throw new RuntimeException("Не удалось обработать ответ.");
      }
    } else {
      throw new IllegalStateException("Нет доступного TTY для отображения результата команды.");
    }
  }
}
