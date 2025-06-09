package com.itmo.mrdvd.mappers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.itmo.mrdvd.Credentials;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.util.Optional;

public class CredentialsJwtMapper implements Mapper<Credentials, String> {
  private final Algorithm algorithm;

  public CredentialsJwtMapper(String secret) {
    this(Algorithm.HMAC256(secret));
  }

  public CredentialsJwtMapper(Algorithm algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public Optional<String> convert(Credentials creds) {
    try {
      return Optional.of(JWT.create().withSubject(creds.getLogin()).sign(this.algorithm));
    } catch (JWTCreationException e) {
      return Optional.empty();
    }
  }
}
