package com.itmo.mrdvd;

import com.itmo.mrdvd.object.LoginPasswordPair;
import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.packet.Packet;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.CacheQueriesStrategy;
import com.itmo.mrdvd.proxy.strategies.FillAuthContextStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.SendServerStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.AuthContext;
import com.itmo.mrdvd.service.executor.AbstractExecutor;
import com.itmo.mrdvd.service.executor.CommandMeta;
import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends AbstractProxy {
  public ClientProxy(
      AbstractSender<Packet> sender,
      AbstractExecutor exec,
      Mapper<? super ServiceQuery, Packet> serial,
      Mapper<Packet, ? extends ServiceQuery> deserial,
      AuthContext<LoginPasswordPair> authContext,
      Mapper<Map<String, String>, CommandMeta> metaMapper) {
    this(sender, exec, serial, deserial, authContext, metaMapper, new HashMap<>());
  }

  public ClientProxy(
      AbstractSender<Packet> sender,
      AbstractExecutor exec,
      Mapper<? super ServiceQuery, Packet> serial,
      Mapper<Packet, ? extends ServiceQuery> deserial,
      AuthContext<LoginPasswordPair> authContext,
      Mapper<Map<String, String>, CommandMeta> metaMapper,
      Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(
        new SendServerStrategy(sender, serial, deserial, new FillAuthContextStrategy(authContext)));
    setStrategy("help", new WrapStrategy(exec));
    setStrategy("exit", new InformStrategy(exec, "Производится выход..."));
    setStrategy("execute_script", new InformStrategy(exec, "Начинается выполнение скрипта..."));
    setStrategy("connect", new InformStrategy(exec, "Связка \"хост-порт\" сохранена."));
    setStrategy(
        "fetch_all",
        new CacheQueriesStrategy(
            sender, exec, serial, deserial, metaMapper, new FillAuthContextStrategy(authContext)));
    setStrategy("login", new InformStrategy(exec, "Связка \"логин-пароль\" сохранена."));
    setStrategy("register", new SendServerStrategy(sender, serial, deserial));
  }
}
