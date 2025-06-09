package com.itmo.mrdvd.mappers;

import com.google.protobuf.Timestamp;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class LocalDatetimeTimestampMapper implements Mapper<LocalDateTime, Timestamp> {
  @Override
  public Optional<Timestamp> convert(LocalDateTime date) {
    Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
    return Optional.of(
        Timestamp.newBuilder()
            .setSeconds(instant.getEpochSecond())
            .setNanos(instant.getNano())
            .build());
  }
}
