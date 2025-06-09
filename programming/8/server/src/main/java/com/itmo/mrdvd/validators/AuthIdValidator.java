package com.itmo.mrdvd.validators;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.itmo.mrdvd.AuthID;

public class AuthIdValidator extends ObjectValidator<AuthID> {
  private final JWTVerifier verifier;

  public boolean validateToken(String token) {
    try {
      this.verifier.verify(token);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }

  public AuthIdValidator(String secret) {
    this(Algorithm.HMAC256(secret));
    AuthID.getDefaultInstance().getToken();
  }

  public AuthIdValidator(Algorithm algorithm) {
    this.verifier = JWT.require(algorithm).build();
    check(AuthID::getToken, String.class, (t) -> this.validateToken(t));
  }
}
