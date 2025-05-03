package com.itmo.mrdvd.service;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.Optional;

/** A service which blindly sends the info and gets the response. */
public abstract class AbstractSender<T, U, R> implements Service {
  protected final Mapper<? super T, U> mapper1;
  protected final Mapper<? extends R, U> mapper2;

  public AbstractSender(Mapper<? super T, U> mapper1, Mapper<? extends R, U> mapper2) {
    this.mapper1 = mapper1;
    this.mapper2 = mapper2;
  }

  /** Saves the endpoint address for future connections. */
  public abstract void setAddress(SocketAddress addr);

  /** Connects to the desired endpoint via passed beforehand address. */
  public abstract void connect() throws IOException;

  /** Sends a message and returns the response from the server. */
  public abstract Optional<? extends R> send(T obj) throws IOException;
}
