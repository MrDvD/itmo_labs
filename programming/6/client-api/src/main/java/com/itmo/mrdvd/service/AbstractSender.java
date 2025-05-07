package com.itmo.mrdvd.service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Optional;

import com.itmo.mrdvd.proxy.mappers.Mapper;

/** A service which blindly sends the info and gets the response. */
public abstract class AbstractSender<T, U, R> implements Service {
  protected final Mapper<? super T, U> serial;
  protected final Mapper<U, ? extends R> deserial;

  public AbstractSender(Mapper<? super T, U> serial, Mapper<U, ? extends R> deserial) {
    this.serial = serial;
    this.deserial = deserial;
  }

  /** Saves the endpoint address for future connections. */
  public abstract void setAddress(SocketAddress addr);

  /** Connects to the desired endpoint via passed beforehand address. */
  public abstract void connect() throws IOException;

  /** Sends a message and returns the response from the server. */
  public abstract Optional<? extends R> send(T obj) throws IOException;
}
