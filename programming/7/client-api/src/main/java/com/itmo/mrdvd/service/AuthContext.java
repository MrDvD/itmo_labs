package com.itmo.mrdvd.service;

import java.util.Optional;

public interface AuthContext<T> {
  public Optional<T> getAuth();

  public void setAuth(T auth);
}