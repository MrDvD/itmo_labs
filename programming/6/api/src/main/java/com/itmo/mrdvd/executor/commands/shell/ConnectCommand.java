package com.itmo.mrdvd.executor.commands.shell;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.service.AbstractSender;

public class ConnectCommand implements Command<Void> {
  protected final AbstractSender<?, ?, ?> sender;

  public ConnectCommand(AbstractSender<?, ?, ?> sender) {
    this.sender = sender;
  }

  @Override
  public Void execute(List<Object> params) throws IllegalStateException {
    if (this.sender == null) {
      throw new IllegalStateException("Не предоставлен отправитель для работы.");
    }
    if (params.size() < 2) {
      throw new IllegalArgumentException("Недостаточное количество аргументов для команды.");
    }
    String host = null;
    Integer port = null;
    try {
      host = (String) params.get(0);
      port = (Integer) params.get(1);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Не удалось распознать аргументы для подключения.");
    }
    /*
      * Of course, it's Tight Coupling, but otherwise i would have to write the variable input parser (~Strategy Pattern or so)
      * because not each socketchannel requires host & port pair (like unix sockets).
      * And, to be honest, i don't really want to mess with this right now (maybe add this feature in future)
      */
    try {
      this.sender.connect(new InetSocketAddress(host, port));
    } catch (RuntimeException | IOException e) {
      throw new RuntimeException("Не удалось подключиться к серверу.");
    }
    return null;
  }

  @Override
  public String name() {
    return "connect";
  }

  @Override
  public String signature() {
    return name() + " {host} {port}";
  }

  @Override
  public String description() {
    return "подключить прокси к серверу по введённой связке хост-порт";
  }
}
