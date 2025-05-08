package com.itmo.mrdvd.public_scope;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.mappers.VariableMapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
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
    // this.mapper.setStrategy(name, strat);
    setDefaultStrategy(new WrapStrategy(exec));
    setStrategy("clear", new InformStrategy(exec, "Коллекция очищена."));
    setStrategy("remove_last", new InformStrategy(exec, "Последний элемент удалён."));
    setStrategy("remove_at", new InformStrategy(exec, "Элемент удалён."));
    setStrategy("remove_by_id", new InformStrategy(exec, "Элемент удалён."));
  }

  public Packet processPacket(Packet p, Mapper<ServiceQuery, Packet> serial) {
    Optional<ServiceQuery> deserial = this.mapper.convert(p);
    if (deserial.isEmpty()) {
      return new EmptyPacket();
    }
    Optional<Packet> result = serial.convert(processQuery(deserial.get()));
    return result.isEmpty() ? new EmptyPacket() : result.get();
  }
}
