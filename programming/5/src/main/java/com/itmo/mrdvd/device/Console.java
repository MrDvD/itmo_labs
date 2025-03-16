package com.itmo.mrdvd.device;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Optional;
import java.util.Scanner;

import com.itmo.mrdvd.device.input.InteractiveInputDevice;

public class Console implements InteractiveInputDevice, OutputDevice {
  private Scanner in;
  private OutputStreamWriter out;

  public Console init() {
    openIn();
    openOut();
    return this;
  }

  @Override
  public IOStatus openIn() {
    this.in = new Scanner(System.in);
    return IOStatus.SUCCESS;
  }

  @Override
  public IOStatus openOut() {
    this.out = new OutputStreamWriter(System.out);
    return IOStatus.SUCCESS;
  }

  @Override
  public IOStatus write(String str) {
    try {
      out.write(str);
      out.flush();
      return IOStatus.SUCCESS;
    } catch (IOException e) {
      return IOStatus.FAILURE;
    }
  }

  @Override
  public IOStatus writeln(String str) {
    return write(str + '\n');
  }

  @Override
  public Optional<String> read() {
    if (in.hasNextLine()) {
      return Optional.of(in.nextLine());
    }
    writeln("");
    openIn();
    return Optional.empty();
  }

  @Override
  public Optional<String> readToken() {
    if (in.hasNext()) {
      String token = in.next();
      if (in.hasNextLine()) {
        in.nextLine();
      }
      return Optional.of(token);
    }
    openIn();
    return Optional.empty();
  }

  @Override
  public Optional<String> readAll() {
    String result = "";
    while (in.hasNext()) {
      result += in.next();
    }
    writeln("");
    openIn();
    if (result.equals("")) {
      return Optional.empty();
    }
    return Optional.of(result);
  }

  @Override
  public Optional<String> readAll(String msg) {
   write(msg);
   return readAll();
  }

  @Override
  public Optional<String> readToken(String msg) {
   write(msg);
   return readToken();
  }

  @Override
  public Optional<String> read(String msg) {
    write(msg);
    return read();
  }

  @Override
  public IOStatus closeIn() {
    this.in.close();
    return IOStatus.SUCCESS;
  }

  @Override
  public IOStatus closeOut() {
    try {
      this.out.close();
      return IOStatus.SUCCESS;
    } catch (IOException e) {
      return IOStatus.FAILURE;
    }
  }
}
