package com.itmo.mrdvd.service;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.Function;

public class ServerClientHandler<T> implements ClientHandler {
  private final Charset chars;
  private final Mapper<? super T, String> serialDTO;
  private final Mapper<String, ? extends T> deserialDTO;
  private final ResponseSender sender;

  public ServerClientHandler(
      Charset chars,
      Mapper<? super T, String> serialDTO,
      Mapper<String, ? extends T> deserialDTO,
      ResponseSender sender) {
    this.chars = chars;
    this.serialDTO = serialDTO;
    this.deserialDTO = deserialDTO;
    this.sender = sender;
  }

  @Override
  public void handleClient(SelectionKey key, ByteBuffer buffer) throws IOException {
    try (SocketChannel client = (SocketChannel) key.channel()) {
      int bytesRead = client.read(buffer);
      if (bytesRead > 0) {
        buffer.flip();
        String receivedData = this.chars.decode(buffer).toString();
        buffer.clear();
        Optional<? extends T> packet = this.deserialDTO.convert(receivedData);
        if (packet.isPresent()) {
          Function<T, T> callback = (Function) key.attachment();
          T responsePacket = callback.apply(packet.get());
          Optional<String> serialized = this.serialDTO.convert(responsePacket);
          if (serialized.isPresent()) {
            this.sender.sendResponse(client, serialized.get());
          }
        }
      }
    }
  }
}
