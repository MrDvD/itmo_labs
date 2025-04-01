package com.itmo.mrdvd.device;

import com.itmo.mrdvd.device.input.InteractiveDataInputDevice;
import java.io.IOException;
import java.util.Optional;

public class DataConsole extends Console implements InteractiveDataInputDevice {
  @Override
  public DataConsole init() {
    return (DataConsole) super.init();
  }

  @Override
  public Optional<Integer> readInt() throws IOException {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      throw new IOException();
    }
    try {
      return Optional.of(Integer.valueOf(token.get()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Long> readLong() throws IOException {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      throw new IOException();
    }
    try {
      return Optional.of(Long.valueOf(token.get()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Float> readFloat() throws IOException {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      throw new IOException();
    }
    try {
      return Optional.of(Float.valueOf(token.get()));
    } catch (NumberFormatException e) {
      return Optional.empty();
    }
  }

  @Override
  public <T extends Enum<T>> Optional<Enum<T>> readEnum(Class<T> cls) throws IOException {
    Optional<String> token = readToken();
    if (token.isEmpty()) {
      throw new IOException();
    }
    try {
      return Optional.of(Enum.valueOf(cls, token.get()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
