package com.itmo.mrdvd.client;

import com.itmo.mrdvd.proxy.ClientProxy;
import com.itmo.mrdvd.proxy.TransportProtocol;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Optional;

public class CollectionClientProxy implements ClientProxy {
  protected final Socket socket;
  protected TransportProtocol protocol;
  protected Optional<InputStreamReader> in;
  protected Optional<OutputStreamWriter> out;

  public CollectionClientProxy(Socket sock, TransportProtocol proto) {
    this.socket = sock;
    this.protocol = proto;
    try {
      this.in = Optional.of(new InputStreamReader(sock.getInputStream()));
      this.out = Optional.of(new OutputStreamWriter(sock.getOutputStream()));
    } catch (IOException e) {
      this.in = Optional.empty();
      this.out = Optional.empty();
    }
  }

  @Override
  public void setProtocol(TransportProtocol proto) {
    if (proto != null) {
      this.protocol = proto;
    }
  }

  @Override
  public void send(String payload) throws RuntimeException {
    if (this.out.isEmpty()) {
      throw new RuntimeException("Ошибка записи в TCP-сокет.");
    }
    try {
      this.out.get().write(this.protocol.wrapPayload(payload));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
