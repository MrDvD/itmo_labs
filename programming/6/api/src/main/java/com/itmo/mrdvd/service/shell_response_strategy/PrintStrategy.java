package com.itmo.mrdvd.service.shell_response_strategy;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractShell;
import java.util.Optional;

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
        tty.get().getOut().writeln((String) r.getBody());
      } catch (ClassCastException e) {
        throw new RuntimeException("Не удалось обработать ответ.");
      }
    } else {
      throw new IllegalStateException("Нет доступного TTY для отображения результата команды.");
    }
  }
}
