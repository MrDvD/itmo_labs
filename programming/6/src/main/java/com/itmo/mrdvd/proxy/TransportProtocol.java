package com.itmo.mrdvd.proxy;

import java.io.InputStream;

public interface TransportProtocol {
  public String wrapPayload(String payload);
  public InputStream getPayload(InputStream stream) throws RuntimeException;
}
