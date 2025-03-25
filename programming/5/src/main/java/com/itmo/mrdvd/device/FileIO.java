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
import java.util.Optional;

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
  public IOStatus openIn() {
   if (path == null) {
      throw new NullPointerException("Не указан путь для открытия файла.");
    } 
   try {
      this.inStream = new FileInputStream(path.toString());
      this.inReader = new BufferedInputStream(inStream);
      return IOStatus.SUCCESS;
    } catch (FileNotFoundException|SecurityException e) {
      return IOStatus.FAILURE;
    }
  }

  @Override
  public IOStatus openOut() {
    try {
      if (path == null) {
        throw new NullPointerException("Не указан путь для открытия файла.");
      }
      this.outStream = new FileOutputStream(path.toString());
      this.outWriter = new OutputStreamWriter(outStream);
      return IOStatus.SUCCESS;
    } catch (FileNotFoundException|SecurityException e) {
      return IOStatus.FAILURE;
    }
  }

  @Override
  public IOStatus closeIn() {
    try {
      this.inReader.close();
      this.inStream.close();
      return IOStatus.SUCCESS;
    } catch (IOException e) {
      return IOStatus.FAILURE;
    }
  }

  @Override
  public IOStatus closeOut() {
    try {
      this.outWriter.close();
      this.outStream.close();
      return IOStatus.SUCCESS;
    } catch (IOException e) {
      return IOStatus.FAILURE;
    }
  }

  public Optional<String> read(String delimiters) {
    if (inReader == null) {
      throw new NullPointerException("Не указан путь для открытия файла.");
    }
    try {
      String result = "";
      while (inReader.available() > 0) {
        inReader.mark(1);
        int chr = inReader.read();
        if (delimiters.indexOf(chr) != -1) { // change logic
          if (chr == '\n') {
            inReader.reset();
          }
          break;
        }
        result += (char) chr;
      }
      if (result.equals("")) {
        return Optional.empty();
      }
      return Optional.of(result);
    } catch (IOException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<String> read() {
    return read("\n");
  }

  @Override
  public Optional<String> readToken() {
   return read(" \n");
  }

  @Override
  public Optional<String> readAll() {
    return read("");
  }

  @Override
  public IOStatus write(String str) {
   if (outWriter == null) {
      throw new NullPointerException("Не указан путь для открытия файла.");
   }
    try {
      outWriter.write(str);
      return IOStatus.SUCCESS;
    } catch (IOException e) {
      return IOStatus.FAILURE;
    }
  }

  @Override
  public IOStatus writeln(String str) {
    return write(str + "\n");
  }

  @Override
  public boolean hasNext() {
   try {
      return inReader.available() > 0;   
   } catch (IOException e) {
      return false;
   }
  }

  @Override
  public void skipLine() {
   try {
       while (inReader.available() > 0) {
         int chr = inReader.read();
         inReader.available();
         if (chr == '\n') {
            break;
         }
       }
   } catch (IOException e) {}
  }
}
