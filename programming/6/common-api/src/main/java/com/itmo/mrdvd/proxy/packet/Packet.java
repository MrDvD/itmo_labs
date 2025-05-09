package com.itmo.mrdvd.proxy.packet;

public interface Packet {
  public void setName(String name);

  public String getName();

  public void setPayload(String payload);

  public String getPayload();
}
