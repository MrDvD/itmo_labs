package com.itmo.mrdvd.mappers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.UserInfo;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.util.Optional;

public class AuthIdUserInfoMapper implements Mapper<AuthID, UserInfo> {
  private final JWTVerifier verifier;

  public AuthIdUserInfoMapper(String secret) {
    this(Algorithm.HMAC256(secret));
  }

  public AuthIdUserInfoMapper(Algorithm algorithm) {
    this.verifier = JWT.require(algorithm).build();
  }

  @Override
  public Optional<UserInfo> convert(AuthID id) {
    try {
      DecodedJWT jwt = this.verifier.verify(id.getToken());
      return Optional.of(UserInfo.newBuilder().setUsername(jwt.getSubject()).build());
    } catch (JWTVerificationException e) {
      return Optional.empty();
    }
  }
}
