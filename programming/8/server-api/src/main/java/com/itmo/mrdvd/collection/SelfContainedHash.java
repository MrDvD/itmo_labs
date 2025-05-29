package com.itmo.mrdvd.collection;

public interface SelfContainedHash {
  public String hash(String secret);

  public boolean compare(String secret, String hash);
}
