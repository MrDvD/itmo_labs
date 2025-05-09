package com.itmo.mrdvd.proxy.mappers;

import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import java.util.Optional;

public class QueryPacketMapper implements Mapper<ServiceQuery, Packet> {
  private final Mapper<Object, String> mapper;

  public QueryPacketMapper(Mapper<Object, String> mapper) {
    this.mapper = mapper;
  }

  @Override
  public Optional<Packet> convert(ServiceQuery obj) {
    if (obj == null) {
      return Optional.empty();
    }
    Optional<String> payload = this.mapper.convert(obj.getArgs());
    if (payload.isEmpty()) {
      throw new RuntimeException("Не удалось сериализовать аргументы запроса.");
    }
    Packet packet = new EmptyPacket();
    packet.setName(obj.getName());
    packet.setPayload(payload.get());
    return Optional.of(packet);
  }
}
