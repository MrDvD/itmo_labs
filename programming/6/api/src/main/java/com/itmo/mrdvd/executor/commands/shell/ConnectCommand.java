package com.itmo.mrdvd.executor.commands.shell;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.ClientProxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

public class ConnectCommand implements UserCommand {
  protected final TTY shell;
  protected final ClientProxy proxy;
  protected List<?> params;

  public ConnectCommand(TTY shell, ClientProxy proxy) {
    this.shell = shell;
    this.proxy = proxy;
  }

  @Override
  public Optional<TTY> getShell() {
    return Optional.ofNullable(this.shell);
  }

  @Override
  public ConnectCommand setShell(TTY shell) {
    return new ConnectCommand(shell, this.proxy);
  }

  @Override
  public Void execute() {
    if (this.proxy == null) {
      throw new IllegalStateException("Не предоставлен прокси для работы.");
    }
    Optional<String> host = Optional.empty();
    Optional<Integer> port = Optional.empty();
    try {
      host = getShell().get().getIn().readToken();
      port = getShell().get().getIn().readInt();
      getShell().get().getIn().skipLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    if (host.isEmpty() || port.isEmpty()) {
      throw new IllegalArgumentException("Не удалось распознать аргументы для подключения.");
    }
    /*
     * Of course, it's Tight Coupling, but otherwise i would have to write the variable input parser (~Strategy Pattern)
     * because not each socketchannel requires host & port pair (like unix sockets).
     * And, to be honest, i don't really want to mess with this right now (maybe add this feature in future)
     */
    try {
      this.proxy.connect(new InetSocketAddress(host.get(), port.get()));
    } catch (RuntimeException | IOException e) {
      throw new RuntimeException("Не удалось подключиться к серверу.");
    }
    getShell().get().getOut().writeln("Успешное подключение к серверу.");
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

  @Override
  public boolean hasArgs() {
    return true;
  }
}
