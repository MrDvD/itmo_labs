package com.itmo.mrdvd.device;

public interface InputDevice {
  public String read();

  public String read(String message);

  public int close();
}
