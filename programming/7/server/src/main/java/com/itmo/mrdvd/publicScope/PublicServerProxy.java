package com.itmo.mrdvd.publicScope;

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
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PublicServerProxy extends AbstractProxy {
  private final VariableMapper<Packet, ServiceQuery, String, List> mapper;

  public PublicServerProxy(
      AbstractExecutor exec, VariableMapper<Packet, ServiceQuery, String, List> mapper) {
    this(exec, mapper, new HashMap<>());
  }

  public PublicServerProxy(
      AbstractExecutor exec,
      VariableMapper<Packet, ServiceQuery, String, List> mapper,
      Map<String, ProxyStrategy> strats) {
    super(strats);
    this.mapper = mapper;
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
    setDefaultStrategy(new WrapStrategy(exec));
    setStrategy("clear", new InformStrategy(exec, "Коллекция очищена."));
    setStrategy("remove_last", new InformStrategy(exec, "Последний элемент удалён."));
    setStrategy("remove_at", new InformStrategy(exec, "Элемент удалён."));
    setStrategy("remove_by_id", new InformStrategy(exec, "Элемент удалён."));
    setStrategy("add", new InformStrategy(exec, "Элемент добавлен."));
    setStrategy("add_if_max", new InformStrategy(exec, "Элемент добавлен."));
    setStrategy("update", new InformStrategy(exec, "Элемент обновлён."));
  }

  public Packet processPacket(Packet p, Mapper<ServiceQuery, Packet> serial) {
    Optional<ServiceQuery> deserial = this.mapper.convert(p);
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
