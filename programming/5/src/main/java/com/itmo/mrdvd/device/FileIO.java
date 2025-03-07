package com.itmo.mrdvd.device;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystem;
import java.nio.file.Path;

public class FileIO extends FileDescriptor {
  private InputStream inStream;
  private BufferedInputStream inReader;
  private OutputStream outStream;
  private OutputStreamWriter outWriter;

  public FileIO(Path path, FileSystem fs) {
   super(path, fs);
  }

  @Override
  public FileIO duplicate() {
   return new FileIO(path, fs);
  }

  @Override
  public int openIn() {
    try {
      if (path == null) {
        return -3;
      }
      this.inStream = new FileInputStream(path.toString());
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
      if (path == null) {
        return -3;
      }
      this.outStream = new FileOutputStream(path.toString());
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
