package com.itmo.mrdvd.device;

public interface FileMeta {
  public void setPath(String filePath);

  public String getPath();

  public String getName();

  public int createFile();
}
