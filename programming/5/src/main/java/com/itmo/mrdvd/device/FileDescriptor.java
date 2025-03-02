package com.itmo.mrdvd.device;

public interface FileDescriptor {
  public FileDescriptor setPath(String filePath);

  public String getPath();

  public String getName();

  public int createFile();
}
