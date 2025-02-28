package com.itmo.mrdvd.device;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileIO implements InputDevice, OutputDevice {
  private final String filePath;
  private InputStream inStream;
  private BufferedInputStream inReader;
  private OutputStream outStream;
  private OutputStreamWriter outWriter;

  public FileIO(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public int openIn() {
    try {
      this.inStream = new FileInputStream(filePath);
      this.inReader = new BufferedInputStream(inStream);
      return 0;
    } catch (FileNotFoundException e) {
      return -1;
    }
  }

  @Override
  public int openOut() {
    try {
      this.outStream = new FileOutputStream(filePath);
      this.outWriter = new OutputStreamWriter(outStream);
      return 0;
    } catch (FileNotFoundException e) {
      return -1;
    }
  }

  @Override
  public int closeIn() {
    try {
      this.inReader.close();
      this.inStream.close();
      return 0;
    } catch (IOException e) {
      return -1;
    }
  }

  @Override
  public int closeOut() {
    try {
      this.outWriter.close();
      this.outStream.close();
      return 0;
    } catch (IOException e) {
      return -1;
    }
  }

  @Override
  public String read() {
    try {
      String result = "";
      while (true) {
        int chr = inReader.read();
        if (chr == -1) {
          break;
        }
        result += (char) chr;
      }
      return result;
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public int write(String str) {
    try {
      outWriter.write(str);
      return 0;
    } catch (IOException e) {
      return -1;
    }
  }

  @Override
  public int writeln(String str) {
    return write(str + "\n");
  }
}
