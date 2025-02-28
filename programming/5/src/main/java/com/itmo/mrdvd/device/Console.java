package com.itmo.mrdvd.device;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Console implements InteractiveInputDevice, OutputDevice {
  private Scanner in;
  private OutputStreamWriter out;

  public Console() {
    openIn();
    openOut();
  }

  @Override
  public int openIn() {
    this.in = new Scanner(System.in);
    return 0;
  }

  @Override
  public int openOut() {
    this.out = new OutputStreamWriter(System.out);
    return 0;
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
    openIn();
    return "\n";
  }

  @Override
  public String read(String msg) {
    write(msg);
    return read();
  }

  //  0: success
  @Override
  public int closeIn() {
    this.in.close();
    return 0;
  }

  //  0: success
  // -1: IOException
  @Override
  public int closeOut() {
    try {
      this.out.close();
      return 0;
    } catch (IOException e) {
      return -1;
    }
  }
}
