package com.itmo.mrdvd.device;

public interface InputDevice {
  public String read();

  public int openIn();

  public int closeIn();
}
