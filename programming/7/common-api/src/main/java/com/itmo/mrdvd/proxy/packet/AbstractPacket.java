package com.itmo.mrdvd.proxy.packet;

/** DTO for transferring information through sockets & i/o streams. */
public abstract class AbstractPacket implements Packet {
  private String name;
  private String payload;

  public AbstractPacket(String name, String payload) {
    this.name = name;
    this.payload = payload;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override
  public String getPayload() {
    return this.payload;
  }
}
