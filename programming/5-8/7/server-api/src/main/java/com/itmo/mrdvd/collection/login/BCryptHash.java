package com.itmo.mrdvd.collection.login;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Version;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BCryptHash implements SelfContainedHash {
  private static final LongPasswordStrategy SHA224_STRATEGY =
      password -> {
        try {
          return MessageDigest.getInstance("SHA-224").digest(password);
        } catch (NoSuchAlgorithmException e) {
          throw new RuntimeException("SHA-224 not available.");
        }
      };

  @Override
  public String hash(String secret) {
    return BCrypt.with(Version.VERSION_2A, SHA224_STRATEGY).hashToString(12, secret.toCharArray());
  }

  @Override
  public boolean compare(String secret, String hash) {
    return BCrypt.verifyer(Version.VERSION_2A, SHA224_STRATEGY)
        .verify(secret.toCharArray(), hash)
        .verified;
  }
}
