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

/**
 * Класс FileIO — реализация InputDevice и OutputDevice для работы с файлами. Отчасти является
 * обёрткой FileInputStream & BufferedInputStream (FileOutputStream & OutputStreamWriter), поскольку
 * они требуются в варианте лабораторной.
 */
public class FileIO extends DataFileDescriptor {
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
  public void openIn() {
    if (path == null) {
      throw new IllegalStateException("Не указан путь для открытия файла.");
    }
    try {
      this.inStream = new FileInputStream(path.toString());
      this.inReader = new BufferedInputStream(inStream);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void openOut() {
    if (path == null) {
      throw new IllegalStateException("Не указан путь для открытия файла.");
    }
    try {
      this.outStream = new FileOutputStream(path.toString());
      this.outWriter = new OutputStreamWriter(outStream);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void closeIn() {
    try {
      this.inReader.close();
      this.inStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void closeOut() {
    try {
      this.outWriter.close();
      this.outStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Optional<String> read(String delimiters) throws IOException {
    if (inReader == null) {
      throw new NullPointerException("Не указан путь для открытия файла.");
    }
    String result = "";
    while (inReader.available() > 0) {
      inReader.mark(1);
      int chr = inReader.read();
      if (delimiters.indexOf(chr) != -1) {
        if (chr == '\n' && !delimiters.equals("\n")) {
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
  }

  @Override
  public Optional<String> read() throws IOException {
    return read("\n");
  }

  @Override
  public Optional<String> readToken() throws IOException {
    return read(" \n");
  }

  @Override
  public Optional<String> readAll() throws IOException {
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
        inReader.mark(1);
        int chr = inReader.read();
        if (chr == '\n') {
          break;
        }
        if (chr != ' ') {
          inReader.reset();
          break;
        }
      }
    } catch (IOException e) {
    }
  }
}
