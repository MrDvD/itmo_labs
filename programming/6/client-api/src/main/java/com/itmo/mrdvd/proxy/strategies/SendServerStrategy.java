package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.mappers.VariableMapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.EmptyServiceQuery;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.AbstractSender;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SendServerStrategy implements ProxyStrategy {
  private final AbstractSender<Packet> sender;
  private final Mapper<? super ServiceQuery, Packet> serial;
  private final VariableMapper<Packet, ? extends ServiceQuery, String, List> deserial;

  public SendServerStrategy(
      AbstractSender<Packet> sender,
      Mapper<? super ServiceQuery, Packet> serial,
      VariableMapper<Packet, ? extends ServiceQuery, String, List> deserial) {
    this.sender = sender;
    this.serial = serial;
    this.deserial = deserial;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    try {
      Optional<? extends Packet> serialized = this.serial.convert(q);
      if (serialized.isEmpty()) {
        throw new RuntimeException("Не удалось сериализовать запрос.");
      }
      this.sender.connect();

      Optional<? extends Packet> r = this.sender.send(serialized.get());
      if (r.isPresent()) {
        Optional<? extends ServiceQuery> deserialized = this.deserial.convert(r.get());
        if (deserialized.isEmpty()) {
          throw new RuntimeException("Не удалось десериализовать ответ.");
        }
        return Optional.of(deserialized.get());
      }
      return Optional.of(new EmptyServiceQuery());
    } catch (IOException e) {
      throw new RuntimeException("Не удалось отправить запрос.");
    }
  }
}
