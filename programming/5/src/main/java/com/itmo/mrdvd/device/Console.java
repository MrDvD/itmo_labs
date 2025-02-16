package com.itmo.mrdvd.device;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Console implements InputDevice, OutputDevice {
   private BufferedInputStream in;
   private OutputStreamWriter out;
   public Console() {
      this.in = new BufferedInputStream(System.in);
      this.out = new OutputStreamWriter(System.out);
   }
   @Override
   public int write(String str) {
      try {
         out.write(str);
         return 0;
      } catch (IOException e) {
         return -1;
      }
   }
   @Override
   public int writeln(String str) {
      return write(str + '\n');
   }
   @Override
   public String read() {
      try {
         return new String(in.readAllBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
         writeln("[ERROR]: Непредвиденная ошибка чтения из консоли.");
         return null;
      }
   }
   @Override
   public String read(String msg) {
      write(msg);
      return read();
   }
}
