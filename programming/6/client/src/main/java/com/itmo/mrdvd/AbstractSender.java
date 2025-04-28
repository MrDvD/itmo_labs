package com.itmo.mrdvd;

import java.io.IOException;
import java.net.SocketAddress;

import com.itmo.mrdvd.proxy.Mapper;
import com.itmo.mrdvd.proxy.Service;

/**
 * A service which blindly sends the info and gets the response.
 */
public abstract class AbstractSender<T, U, R> implements Service {
  protected final Mapper<T, U> mapper1;
  protected final Mapper<R, U> mapper2;

  public AbstractSender(Mapper<T, U> mapper1, Mapper<R, U> mapper2) {
    this.mapper1 = mapper1;
    this.mapper2 = mapper2;
  }

  /** Connects to the desired endpoint via passed address. */
  public abstract void setReceiver(SocketAddress addr) throws IOException;

  /** Sends a message and returns the response from the server. */
  public abstract R send(T obj) throws IOException;
}
