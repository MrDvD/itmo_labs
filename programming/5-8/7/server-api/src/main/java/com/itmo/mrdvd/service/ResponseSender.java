package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface ResponseSender {
  public void sendResponse(SocketChannel client, String response) throws IOException;
}
