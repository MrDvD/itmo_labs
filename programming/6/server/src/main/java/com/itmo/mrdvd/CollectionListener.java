package com.itmo.mrdvd;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.Mapper;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractListener;

public class CollectionListener extends AbstractListener<Query, String, Response> {
  protected final Map<SelectionKey, ByteBuffer> buffers;
  protected final int bufferSize;
  protected final Charset chars;
  protected boolean isOpen;

  public CollectionListener(Selector selector, Function<Query, Response> callback, Mapper<Query, String> mapper1, Mapper<Response, String> mapper2) {
    this(selector, callback, mapper1, mapper2, 4096, Charset.forName("UTF-8"));
  }

  public CollectionListener(Selector selector, Function<Query, Response> callback, Mapper<Query, String> mapper1, Mapper<Response, String> mapper2, int bufferSize, Charset chars) {
    this(selector, callback, mapper1, mapper2, bufferSize, chars, new HashMap<>(), new HashMap<>());
  }
  
  public CollectionListener(Selector selector,
      Function<Query, Response> callback,
      Mapper<Query, String> mapper1,
      Mapper<Response, String> mapper2,
      int bufferSize,
      Charset chars,
      Map<SelectionKey, AbstractSelectableChannel> sockets,
      Map<SelectionKey, ByteBuffer> buffers) {
    super(selector, callback, mapper1, mapper2, sockets);
    this.buffers = buffers;
    this.bufferSize = bufferSize;
    this.chars = chars;
  }

  /** 
   * Waits for incoming connections in a non-blocking way.
   */
  @Override
  public void start() throws IllegalStateException, RuntimeException {
    if (this.mapper1 == null) {
      throw new IllegalStateException("Не предоставлен маппер входящих запросов.");
    }
    if (this.mapper2 == null) {
      throw new IllegalStateException("Не предоставлен маппер исходящих запросов.");
    }
    if (this.callback == null) {
      throw new IllegalStateException("Не предоставлен метод для обработки входящих запросов.");
    }
    this.isOpen = true;
    try {
      while (this.isOpen) {
        this.selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        for (SelectionKey key : keys) {
          if (key.isAcceptable()) {
            SocketChannel client = ((ServerSocketChannel) this.sockets.get(key)).accept();
            client.configureBlocking(false);
            SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
            this.sockets.put(clientKey, client);
            this.buffers.put(clientKey, ByteBuffer.allocate(bufferSize));
          }
          if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            int chr = client.read(this.buffers.get(key));
            if (chr == -1) {
              Optional<Query> q = this.mapper1.unwrap(this.buffers.get(key).toString());
              this.buffers.get(key).clear();
              if (q.isPresent()) {
                Response r = this.callback.apply(q.get());
                client.write(this.chars.encode(this.mapper2.wrap(r)));
              }
              client.close();
            }
          }
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
