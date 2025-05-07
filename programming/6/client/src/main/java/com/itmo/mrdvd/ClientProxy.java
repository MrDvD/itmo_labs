package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.proxy.strategies.CacheQueriesStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.SendServerStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class ClientProxy extends AbstractProxy {
  public ClientProxy(
      AbstractSender<Query, String, Response> sender, AbstractExecutor exec) {
    this(sender, exec, new HashMap<>());
  }

  public ClientProxy(
      AbstractSender<Query, String, Response> sender,
      AbstractExecutor exec,
      Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(new SendServerStrategy(sender));
    setStrategy("help", new WrapStrategy(exec));
    setStrategy("exit", new InformStrategy(exec, "Производится выход..."));
    setStrategy("execute_script", new InformStrategy(exec, "Начинается выполнение скрипта..."));
    setStrategy("connect", new InformStrategy(exec, "Связка \"хост-порт\" сохранена."));
    setStrategy("fetch_all", new CacheQueriesStrategy(sender, exec));
  }
}
