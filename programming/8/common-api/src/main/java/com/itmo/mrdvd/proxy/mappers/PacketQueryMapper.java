package com.itmo.mrdvd.proxy.mappers;

import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import java.util.List;
import java.util.Optional;

public class PacketQueryMapper implements Mapper<Packet, ServiceQuery> {
  private final Mapper<String, List> mapper;

  public PacketQueryMapper(Mapper<String, List> mapper) {
    this.mapper = mapper;
  }

  @Override
  public Optional<ServiceQuery> convert(Packet obj) {
    if (obj == null) {
      return Optional.empty();
    }
    Optional<List> result = Optional.empty();
    result = this.mapper.convert(obj.getPayload());
    return Optional.of(ServiceQuery.of(obj.getName(), result.isEmpty() ? List.of() : result.get()));
  }
}
