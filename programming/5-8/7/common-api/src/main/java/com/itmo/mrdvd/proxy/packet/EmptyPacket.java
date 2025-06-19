package com.itmo.mrdvd.proxy.packet;

public class EmptyPacket extends AbstractPacket {
  public EmptyPacket() {
    super("empty_packet", "");
  }
}
