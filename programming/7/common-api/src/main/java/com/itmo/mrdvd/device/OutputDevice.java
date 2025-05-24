package com.itmo.mrdvd.device;

public interface OutputDevice {
  public IOStatus write(String str);

  public IOStatus writeln(String str);

  public void openOut();

  public void closeOut();
}
