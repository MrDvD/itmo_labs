package com.itmo.mrdvd.device;

import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Optional;

import com.itmo.mrdvd.device.input.DataInputDevice;

public abstract class FileDescriptor implements DataInputDevice, OutputDevice, FileMeta {
   protected Path path;
   protected FileSystem fs;

  public FileDescriptor(Path path, FileSystem fs) {
   this.path = path;
   this.fs = fs;
  }

  public abstract FileDescriptor duplicate();

  public FileSystem getFs() {
   return this.fs;
  }

  @Override
  public Optional<Path> setPath(String filePath) {
   try {
      this.path = getFs().getPath(filePath);
      return Optional.of(this.path);
   } catch (InvalidPathException e) {
      return Optional.empty();
   }
  }

  @Override
  public Optional<Path> getPath() {
    return Optional.ofNullable(this.path);
  }
}
