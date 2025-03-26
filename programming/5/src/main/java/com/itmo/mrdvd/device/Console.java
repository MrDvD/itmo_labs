package com.itmo.mrdvd.device;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Optional;
import java.util.Scanner;

import com.itmo.mrdvd.device.input.InteractiveInputDevice;

public abstract class Console implements InteractiveInputDevice {
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
  public boolean hasNext() {
    return in.hasNext();
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
  public Optional<String> read() throws IOException {
    if (in.hasNextLine()) {
      return Optional.of(in.nextLine());
    }
    throw new IOException();
  }

  @Override
  public Optional<String> readToken() throws IOException {
    if (in.hasNext()) {
      return Optional.of(in.next());
    }
    throw new IOException();
  }

  @Override
  public void skipLine() {
    if (in.hasNextLine()) {
      in.nextLine();
    }
    openIn();
  }

  @Override
  public Optional<String> readAll() throws IOException {
    String result = "";
    while (in.hasNext()) {
      result += in.next();
    }
    writeln("");
    openIn();
    if (result.equals("")) {
      throw new IOException();
    }
    return Optional.of(result);
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
