package com.itmo.mrdvd.mappers;

import com.itmo.mrdvd.AuthID;
import com.itmo.mrdvd.service.ContextKeys;
import io.grpc.Context;
import java.util.Optional;

public class ContextAuthIdMapper implements Mapper<Context, AuthID> {
  @Override
  public Optional<AuthID> convert(Context ctx) {
    Object tokenValue = ContextKeys.TOKEN.getKey().get(ctx);
    if (tokenValue instanceof String stringTokenValue) {
      return Optional.of(AuthID.newBuilder().setToken(stringTokenValue).build());
    }
    return Optional.empty();
  }
}
