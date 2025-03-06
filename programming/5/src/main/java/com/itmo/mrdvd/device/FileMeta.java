package com.itmo.mrdvd.device;

import java.nio.file.Path;
import java.util.Optional;

public interface FileMeta {
  public Optional<Path> setPath(String filePath);

  public Optional<Path> getPath();
}
