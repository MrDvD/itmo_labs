package com.itmo.mrdvd.proxy;

import java.util.Optional;

public interface Proxy {
  public void setProtocol(TransportProtocol proto);

  public Optional<TransportProtocol> getProtocol();
}
