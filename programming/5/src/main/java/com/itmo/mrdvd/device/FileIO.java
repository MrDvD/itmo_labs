package com.itmo.mrdvd.device;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileIO implements InputDevice, OutputDevice, FileDescriptor {
  private String filePath;
  private InputStream inStream;
  private BufferedInputStream inReader;
  private OutputStream outStream;
  private OutputStreamWriter outWriter;

  @Override
  public FileIO setPath(String filePath) {
    this.filePath = filePath;
    return this;
  }

  @Override
  public int createFile() {
    try {
      return new File(filePath).createNewFile() ? 0 : -3;
    } catch (IOException e) {
      return -1;
    } catch (SecurityException e) {
      return -2;
    }
  }

  @Override
  public int openIn() {
    try {
      if (filePath == null) {
        return -3;
      }
      this.inStream = new FileInputStream(filePath);
      this.inReader = new BufferedInputStream(inStream);
      return 0;
    } catch (FileNotFoundException e) {
      return -1;
    } catch (SecurityException e) {
      return -2;
    }
  }

  @Override
  public int openOut() {
    try {
      if (filePath == null) {
        return -3;
      }
      this.outStream = new FileOutputStream(filePath);
      this.outWriter = new OutputStreamWriter(outStream);
      return 0;
    } catch (FileNotFoundException e) {
      return -1;
    } catch (SecurityException e) {
      return -2;
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
      if (inReader == null) {
        return null;
      }
      String result = "";
      while (inReader.available() > 0) {
        int chr = inReader.read();
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
      if (outWriter != null) {
        outWriter.write(str);
        return 0;
      } else {
        return -2;
      }
    } catch (IOException e) {
      return -1;
    }
  }

  @Override
  public int writeln(String str) {
    return write(str + "\n");
  }
}
