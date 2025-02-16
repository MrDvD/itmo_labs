package com.itmo.mrdvd.device;

public interface OutputDevice {
   public int write(String str);
   public int writeln(String str);
   public void close();
}