package com.itmo.mrdvd.publicScope;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.EmptyPacket;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.AuthWrapperStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.LoginCheckStrategy;
import com.itmo.mrdvd.proxy.strategies.ObjectWrapperStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PublicServerProxy extends AbstractProxy {
  public PublicServerProxy(
      AbstractExecutor exec, Mapper<Map<String, String>, LoginPasswordPair> authMapper,
      Mapper<Map<String, Object>, ?> objMapper) {
    this(exec, authMapper, objMapper, new HashMap<>());
  }

  public PublicServerProxy(AbstractExecutor exec, Mapper<Map<String, String>, LoginPasswordPair> authMapper, Mapper<Map<String, Object>, ?> objMapper, Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(new WrapStrategy(exec, new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper))));
    setStrategy(
        "clear",
        new InformStrategy(exec, "Коллекция очищена.", new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper))));
    setStrategy(
        "remove_last", new InformStrategy(exec, "Последний элемент удалён.", new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper))));
    setStrategy(
        "remove_at",
        new InformStrategy(exec, "Элемент удалён.", new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper))));
    setStrategy(
        "remove_by_id",
        new InformStrategy(exec, "Элемент удалён.", new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper))));
    setStrategy(
        "add",
        new InformStrategy(exec, "Элемент добавлен.", new ObjectWrapperStrategy<>(objMapper, new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper)))));
    setStrategy(
        "add_if_max",
        new InformStrategy(exec, "Элемент добавлен.", new ObjectWrapperStrategy<>(objMapper, new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper)))));
    setStrategy(
        "update",
        new InformStrategy(exec, "Элемент обновлён.", new ObjectWrapperStrategy<>(objMapper, new LoginCheckStrategy(this, "login", new AuthWrapperStrategy(authMapper)))));
    setStrategy("login", new WrapStrategy(exec));
    setStrategy("register", new InformStrategy(exec, "Пользователь зарегистрирован.", new AuthWrapperStrategy(authMapper)));
  }

  public Packet processPacket(Packet p, Mapper<ServiceQuery, Packet> serial, Mapper<Packet, ServiceQuery> deserial) {
    Optional<ServiceQuery> incoming = deserial.convert(p);
    if (incoming.isEmpty()) {
      return new EmptyPacket();
    }
    Optional<ServiceQuery> ans = processQuery(incoming.get());
    if (ans.isEmpty()) {
      return new EmptyPacket();
    }
    Optional<Packet> result = serial.convert(ans.get());
    return result.isEmpty() ? new EmptyPacket() : result.get();
  }
}
