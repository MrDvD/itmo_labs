package com.itmo.mrdvd.device;

public interface InteractiveInputDevice extends InputDevice {
  public String read(String message);
}
