package com.itmo.mrdvd.commands;

import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.executor.Command;
import java.net.InetSocketAddress;
import java.util.List;

public class ConnectCommand implements Command<Void> {
  protected final AbstractSender<?> sender;

  public ConnectCommand(AbstractSender<?> sender) {
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
    this.sender.setAddress(new InetSocketAddress(host, port));
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
    return "сохранить связку хост-порт для будущих запросов к коллекции";
  }
}
