package com.itmo.mrdvd.service.shell.response_strategy;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.shell.AbstractShell;

public class PrintStrategy implements ShellResponseStrategy {
  private final AbstractShell shell;

  public PrintStrategy(AbstractShell shell) {
    this.shell = shell;
  }

  @Override
  public void make(Response r) throws IllegalStateException {
    Optional<TTY> tty = this.shell.getTty();
    if (tty.isPresent()) {
      try {
        List<Object> lst = r.getBody();
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
