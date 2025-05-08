package com.itmo.mrdvd.private_scope;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.mappers.VariableMapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.service_query.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.FetchAllStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.RedirectToStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrivateServerProxy extends AbstractProxy {
  private final VariableMapper<Packet, ServiceQuery, String, List> mapper;

  public PrivateServerProxy(
      AbstractExecutor exec,
      Proxy other,
      VariableMapper<Packet, ServiceQuery, String, List> mapper) {
    this(exec, other, mapper, new HashMap<>());
  }

  public PrivateServerProxy(
      AbstractExecutor exec,
      Proxy other,
      VariableMapper<Packet, ServiceQuery, String, List> mapper,
      Map<String, ProxyStrategy> strats) {
    super(strats);
    this.mapper = mapper;
    // this.mapper.setStrategy(name, strat);
    setDefaultStrategy(new RedirectToStrategy(other));
    setStrategy("fetch_all", new FetchAllStrategy(exec, other));
    setStrategy("save", new InformStrategy(exec, "Коллекция сохранена."));
    setStrategy("load", new InformStrategy(exec, "Коллекция загружена."));
    // shutdown
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
