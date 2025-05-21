package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.EmptyServiceQuery;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.service.AbstractSender;
import java.io.IOException;
import java.util.Optional;

public class SendServerStrategy implements ProxyStrategy {
  private final AbstractSender<Packet> sender;
  private final Mapper<? super ServiceQuery, Packet> serial;
  private final Mapper<Packet, ? extends ServiceQuery> deserial;
  private final ProxyStrategy prev;

  public SendServerStrategy(
      AbstractSender<Packet> sender,
      Mapper<? super ServiceQuery, Packet> serial,
      Mapper<Packet, ? extends ServiceQuery> deserial) {
    this(sender, serial, deserial, null);
  }

  public SendServerStrategy(
      AbstractSender<Packet> sender,
      Mapper<? super ServiceQuery, Packet> serial,
      Mapper<Packet, ? extends ServiceQuery> deserial,
      ProxyStrategy prev) {
    this.sender = sender;
    this.serial = serial;
    this.deserial = deserial;
    this.prev = prev;
  }

  @Override
  public Optional<ServiceQuery> make(ServiceQuery q) {
    if (this.prev != null) {
      Optional<ServiceQuery> prevResult = this.prev.make(q);
      if (prevResult.isPresent()) {
        q = prevResult.get();
      }
    }
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
