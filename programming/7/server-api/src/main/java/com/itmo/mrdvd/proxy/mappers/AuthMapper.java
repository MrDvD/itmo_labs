package com.itmo.mrdvd.proxy.mappers;

import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.object.LoginPasswordPair;

public class AuthMapper implements Mapper<Map<String, String>, LoginPasswordPair> {
  @Override
  public Optional<LoginPasswordPair> convert(Map<String, String> obj) {
    if (!obj.containsKey("login") || !obj.containsKey("password")) {
      return Optional.empty();
    }
    LoginPasswordPair pair = new LoginPasswordPair();
    pair.setLogin(obj.get("login"));
    pair.setPassword(obj.get("password"));
    return Optional.of(pair);
  }
}
