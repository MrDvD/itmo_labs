package com.itmo.mrdvd.device;

import java.nio.file.FileSystem;
import java.nio.file.Path;

public abstract class FileDescriptor implements InputDevice, OutputDevice, FileMeta {
   protected Path path;
   protected FileSystem fs;

  public FileDescriptor(Path path, FileSystem fs) {
   this.path = path;
   this.fs = fs;
  }

  public abstract FileDescriptor create();

  public FileSystem getFs() {
   return this.fs;
  }
}
