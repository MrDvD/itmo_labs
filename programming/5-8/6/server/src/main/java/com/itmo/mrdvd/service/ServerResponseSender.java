package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServerResponseSender implements ResponseSender {
  private final Charset chars;

  public ServerResponseSender(Charset chars) {
    this.chars = chars;
  }

  @Override
  public void sendResponse(SocketChannel client, String response) throws IOException {
    client.write(this.chars.encode(CharBuffer.wrap(response)));
  }
}
