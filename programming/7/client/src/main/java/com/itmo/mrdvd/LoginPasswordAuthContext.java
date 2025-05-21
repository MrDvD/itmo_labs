package com.itmo.mrdvd;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.service.AuthContext;
import java.util.Optional;

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
