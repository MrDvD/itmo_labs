package com.itmo.mrdvd;

import java.util.Optional;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.AuthContext;

public class LoginPasswordAuthContext implements AuthContext<LoginPasswordPair> {
  private LoginPasswordPair auth;

  @Override
  public Optional<LoginPasswordPair> getAuth() {
    return Optional.ofNullable(auth);
  }

  @Override
  public void setAuth(LoginPasswordPair auth) {
    this.auth = auth;
  }
}
