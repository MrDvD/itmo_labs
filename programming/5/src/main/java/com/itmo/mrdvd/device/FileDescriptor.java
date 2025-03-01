package com.itmo.mrdvd.device;

public interface FileDescriptor {
  public FileDescriptor setPath(String filePath);

  public int createFile();
}
