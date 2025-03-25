package com.itmo.mrdvd.device;

import com.itmo.mrdvd.device.input.DataInputDevice;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Optional;

public abstract class DataFileDescriptor extends FileDescriptor implements DataInputDevice {
  public DataFileDescriptor(Path path, FileSystem fs) {
    super(path, fs);
  }

  @Override
  public abstract DataFileDescriptor duplicate();

  @Override
  public Optional<Integer> readInt() {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Integer.valueOf(token.get()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Long> readLong() {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Long.valueOf(token.get()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Float> readFloat() {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Float.valueOf(token.get()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public <T extends Enum<T>> Optional<Enum<T>> readEnum(Class<T> cls) {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(Enum.valueOf(cls, token.get()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
