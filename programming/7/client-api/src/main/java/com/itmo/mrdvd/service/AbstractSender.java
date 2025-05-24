package com.itmo.mrdvd.service;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.Optional;

/** A service which blindly sends the info and gets the response. */
public abstract class AbstractSender<T> implements Service {
  protected final Mapper<? super T, String> serial;
  protected final Mapper<String, ? extends T> deserial;

  public AbstractSender(Mapper<? super T, String> serial, Mapper<String, ? extends T> deserial) {
    this.serial = serial;
    this.deserial = deserial;
  }

  /** Saves the endpoint address for future connections. */
  public abstract void setAddress(SocketAddress addr);

  /** Connects to the desired endpoint via passed beforehand address. */
  public abstract void connect() throws IOException;

  /** Sends a message and returns the response from the server. */
  public abstract Optional<? extends T> send(T obj) throws IOException;
}
