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

public class FileIO extends FileDescriptor {
  private String filePath;
  private InputStream inStream;
  private BufferedInputStream inReader;
  private OutputStream outStream;
  private OutputStreamWriter outWriter;

  @Override
  public void setPath(String filePath) {
    this.filePath = new File(filePath).getAbsolutePath();
  }

  @Override
  public String getPath() {
    return this.filePath;
  }

  @Override
  public String getName() {
    return new File(getPath()).getName();
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

  public String read(boolean byLine) {
    try {
      if (inReader == null) {
        return null;
      }
      String result = "";
      while (inReader.available() > 0) {
        int chr = inReader.read();
        if (byLine && chr == '\n') {
          break;
        }
        result += (char) chr;
      }
      if (result.equals("")) {
        return null;
      }
      return result;
    } catch (IOException e) {
      return null;
    }
  }

  @Override
  public String read() {
    return read(true);
  }

  @Override
  public String readAll() {
    return read(false);
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
