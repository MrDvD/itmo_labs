package com.itmo.mrdvd.device;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Console implements InputDevice, OutputDevice {
  private Scanner in;
  private OutputStreamWriter out;

  public Console() {
    this.out = new OutputStreamWriter(System.out);
    initIn();
  }

  private void initIn() {
    this.in = new Scanner(System.in);
  }

  //  0: success
  // -1: IOException
  @Override
  public int write(String str) {
    try {
      out.write(str);
      out.flush();
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
    if (in.hasNextLine()) {
      return in.nextLine();
    }
    writeln("");
    initIn();
    return "\n";
  }

  @Override
  public String read(String msg) {
    write(msg);
    return read();
  }

  //  0: success
  // -1: IOException
  @Override
  public int close() {
    try {
      this.in.close();
      this.out.close();
      return 0;
    } catch (IOException e) {
      return -1;
    }
  }
}
