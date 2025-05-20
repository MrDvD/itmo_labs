package com.itmo.mrdvd.publicScope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.UpdateDTO;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.mappers.ObjectDeserializer;
import com.itmo.mrdvd.proxy.mappers.VariableMapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.LoginCheckStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class PublicServerProxy extends AbstractProxy {
  private final VariableMapper<Packet, ServiceQuery, String, List> mapper;
  private final Mapper<ServiceQuery, ServiceQuery> authMapper;

  public PublicServerProxy(
      AbstractExecutor exec, VariableMapper<Packet, ServiceQuery, String, List> mapper, Mapper<ServiceQuery, ServiceQuery> authMapper) {
    this(exec, mapper, authMapper, new HashMap<>());
  }

  public PublicServerProxy(
      AbstractExecutor exec,
      VariableMapper<Packet, ServiceQuery, String, List> mapper,
      Mapper<ServiceQuery, ServiceQuery> authMapper,
      Map<String, ProxyStrategy> strats) {
    super(strats);
    this.mapper = mapper;
    this.authMapper = authMapper;
    this.mapper.setStrategy(
        "add",
        new ObjectDeserializer<>(
            new XmlMapper(),
            TypeFactory.defaultInstance().constructCollectionType(List.class, Ticket.class)));
    this.mapper.setStrategy(
        "add_if_max",
        new ObjectDeserializer<>(
            new XmlMapper(),
            TypeFactory.defaultInstance().constructCollectionType(List.class, Ticket.class)));
    this.mapper.setStrategy(
        "update",
        new ObjectDeserializer<>(
            new XmlMapper(),
            TypeFactory.defaultInstance()
                .constructCollectionType(
                    List.class,
                    TypeFactory.defaultInstance()
                        .constructParametricType(UpdateDTO.class, Ticket.class))));
    setDefaultStrategy(new LoginCheckStrategy(this, "login", new WrapStrategy(exec)));
    setStrategy(
        "clear",
        new LoginCheckStrategy(this, "login", new InformStrategy(exec, "Коллекция очищена.")));
    setStrategy(
        "remove_last",
        new LoginCheckStrategy(
            this, "login", new InformStrategy(exec, "Последний элемент удалён.")));
    setStrategy(
        "remove_at",
        new LoginCheckStrategy(this, "login", new InformStrategy(exec, "Элемент удалён.")));
    setStrategy(
        "remove_by_id",
        new LoginCheckStrategy(this, "login", new InformStrategy(exec, "Элемент удалён.")));
    setStrategy(
        "add",
        new LoginCheckStrategy(this, "login", new InformStrategy(exec, "Элемент добавлен.")));
    setStrategy(
        "add_if_max",
        new LoginCheckStrategy(this, "login", new InformStrategy(exec, "Элемент добавлен.")));
    setStrategy(
        "update",
        new LoginCheckStrategy(this, "login", new InformStrategy(exec, "Элемент обновлён.")));
    setStrategy("login", new WrapStrategy(exec));
  }

  public Packet processPacket(Packet p, Mapper<ServiceQuery, Packet> serial) {
    Optional<ServiceQuery> deserial = this.mapper.convert(p);
    if (deserial.isEmpty()) {
      return new EmptyPacket();
    }
    deserial = this.authMapper.convert(deserial.get());
    if (deserial.isEmpty()) {
      return new EmptyPacket();
    }
    Optional<ServiceQuery> ans = processQuery(deserial.get());
    if (ans.isEmpty()) {
      return new EmptyPacket();
    }
    Optional<Packet> result = serial.convert(ans.get());
    return result.isEmpty() ? new EmptyPacket() : result.get();
  }
}
