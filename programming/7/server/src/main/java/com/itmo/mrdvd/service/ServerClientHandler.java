package com.itmo.mrdvd.service;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Function;

public class ServerClientHandler<T> implements ClientHandler {
  private final Charset chars;
  private final Mapper<? super T, String> serialDTO;
  private final Mapper<String, ? extends T> deserialDTO;
  private final ResponseSender sender;
  private final ExecutorService processingPool;
  private final ReadWriteLock selectorLock;

  public ServerClientHandler(
      Charset chars,
      Mapper<? super T, String> serialDTO,
      Mapper<String, ? extends T> deserialDTO,
      ResponseSender sender,
      ExecutorService processingPool,
      ReadWriteLock selectorLock) {
    this.chars = chars;
    this.serialDTO = serialDTO;
    this.deserialDTO = deserialDTO;
    this.sender = sender;
    this.processingPool = processingPool;
    this.selectorLock = selectorLock;
  }

  @Override
  public void handleClient(SelectionKey key, ByteBuffer buffer) {
    // System.out.println("Trying to get the SelectorLock in handleclient");
    this.selectorLock.writeLock().lock();
    if (key.isValid()) {
      key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
      key.selector().wakeup();
    }
    this.selectorLock.writeLock().unlock();
    // System.out.println("Released the SelectorLock in handleclient");
    processingPool.submit(
        () -> {
          SocketChannel client = (SocketChannel) key.channel();
          try {
            int bytesRead = client.read(buffer);
            System.out.printf("Handling client: %s\n", client.getRemoteAddress());
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
                  key.cancel();
                }
              }
            }
          } catch (IOException e) {
            throw new RuntimeException(e);
          } finally {
            // System.out.println("finally Getting selectorLock in handle");
            this.selectorLock.writeLock().lock();
            if (key.isValid()) {
              key.interestOps(key.interestOps() | SelectionKey.OP_READ);
              key.selector().wakeup();
            }
            this.selectorLock.writeLock().unlock();
            // System.out.println("finally released selectorLock in accept");
          }
        });
  }
}
