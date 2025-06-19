package com.itmo.mrdvd.collection.login;

public interface SelfContainedHash {
  public String hash(String secret);

  public boolean compare(String secret, String hash);
}
