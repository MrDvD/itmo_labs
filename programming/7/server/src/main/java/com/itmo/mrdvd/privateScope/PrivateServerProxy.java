package com.itmo.mrdvd.privateScope;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.mappers.VariableMapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.FetchAllStrategy;
import com.itmo.mrdvd.proxy.strategies.IgnoreStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.LoginCheckStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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
    setDefaultStrategy(new IgnoreStrategy());
    setStrategy(
        "fetch_all", new LoginCheckStrategy(other, "login", new FetchAllStrategy(exec, other)));
    setStrategy(
        "save",
        new LoginCheckStrategy(other, "login", new InformStrategy(exec, "Коллекция сохранена.")));
    setStrategy(
        "load",
        new LoginCheckStrategy(other, "login", new InformStrategy(exec, "Коллекция загружена.")));
    setStrategy(
        "shutdown",
        new LoginCheckStrategy(
            other, "login", new InformStrategy(exec, "Сервер завершил работу.")));
  }

  public Packet processPacket(
      Packet p, Mapper<ServiceQuery, Packet> serial, Function<Packet, Packet> redirect) {
    Optional<ServiceQuery> raw = this.mapper.convert(p);
    if (raw.isEmpty()) {
      return redirect.apply(p);
    }
    Optional<ServiceQuery> ans = processQuery(raw.get());
    if (ans.isEmpty()) {
      return redirect.apply(p);
    }
    Optional<Packet> result = serial.convert(ans.get());
    return result.isEmpty() ? new EmptyPacket() : result.get();
  }
}
