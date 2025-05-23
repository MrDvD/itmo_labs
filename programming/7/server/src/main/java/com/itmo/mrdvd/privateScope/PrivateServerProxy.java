package com.itmo.mrdvd.privateScope;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.AuthWrapperStrategy;
import com.itmo.mrdvd.proxy.strategies.FetchAllStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.LoginCheckStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.RedirectStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PrivateServerProxy extends AbstractProxy {
  public PrivateServerProxy(
      AbstractExecutor exec,
      Proxy other,
      Mapper<Map<String, String>, LoginPasswordPair> authMapper) {
    this(exec, other, authMapper, new HashMap<>());
  }

  public PrivateServerProxy(
      AbstractExecutor exec,
      Proxy other,
      Mapper<Map<String, String>, LoginPasswordPair> authMapper,
      Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(new RedirectStrategy(other));
    setStrategy(
        "fetch_all",
        new FetchAllStrategy(
            exec,
            other,
            new LoginCheckStrategy(other, "login", new AuthWrapperStrategy(authMapper))));
    setStrategy(
        "shutdown",
        new InformStrategy(
            exec,
            "Сервер завершил работу.",
            new LoginCheckStrategy(other, "login", new AuthWrapperStrategy(authMapper))));
    setStrategy(
        "clear",
        new InformStrategy(
            exec,
            "Коллекция очищена.",
            new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper))));
  }

  public Packet processPacket(
      Packet p, Mapper<ServiceQuery, Packet> serial, Mapper<Packet, ServiceQuery> deserial) {
    Optional<ServiceQuery> raw = deserial.convert(p);
    if (raw.isEmpty()) {
      return new EmptyPacket();
    }
    Optional<ServiceQuery> ans = processQuery(raw.get());
    if (ans.isEmpty()) {
      return new EmptyPacket();
    }
    Optional<Packet> result = serial.convert(ans.get());
    return result.isEmpty() ? new EmptyPacket() : result.get();
  }
}
