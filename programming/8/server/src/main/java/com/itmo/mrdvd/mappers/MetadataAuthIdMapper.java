package com.itmo.mrdvd.mappers;

import com.itmo.mrdvd.AuthID;
import io.grpc.Metadata;
import java.util.Optional;

public class MetadataAuthIdMapper implements Mapper<Metadata, AuthID> {
  private final Metadata.Key<?> token;

  public MetadataAuthIdMapper(Metadata.Key<?> token) {
    this.token = token;
  }

  @Override
  public Optional<AuthID> convert(Metadata meta) {
    Object tokenValue = meta.get(this.token);
    if (tokenValue instanceof String stringTokenValue) {
      return Optional.of(AuthID.newBuilder().setToken(stringTokenValue).build());
    }
    return Optional.empty();
  }
}
