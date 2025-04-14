package com.itmo.mrdvd.device;

public interface OutputDevice {
  public IOStatus write(String str);

  public IOStatus writeln(String str);

  public IOStatus openOut() throws NullPointerException;

  public IOStatus closeOut();
}
