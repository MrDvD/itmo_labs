package com.itmo.mrdvd.proxy.mappers;

import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.service_query.AbstractServiceQuery;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PacketQueryMapper implements VariableMapper<Packet, ServiceQuery, String, List> {
  private final Map<String, Mapper<String, List>> mappers;

  public PacketQueryMapper() {
    this(new HashMap<>());
  }

  public PacketQueryMapper(Map<String, Mapper<String, List>> mappers) {
    this.mappers = mappers;
  }

  @Override
  public void setStrategy(String name, Mapper<String, List> mapper) {
    this.mappers.put(name, mapper);
  }

  @Override
  public Optional<ServiceQuery> convert(Packet obj) {
    if (obj == null) {
      return Optional.empty();
    }
    Optional<List> result = Optional.empty();
    if (this.mappers.containsKey(obj.getName())) {
      result = this.mappers.get(obj.getName()).convert(obj.getPayload());
    }
    return Optional.of(
        new AbstractServiceQuery(obj.getName(), result.isEmpty() ? List.of() : result.get()) {});
  }
}
