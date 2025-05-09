package com.itmo.mrdvd.service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public interface ClientHandler {
  public void handleClient(SelectionKey key, ByteBuffer buffer) throws IOException;
}
