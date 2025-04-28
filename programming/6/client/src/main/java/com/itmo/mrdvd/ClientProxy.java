package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.proxy.strategies.ClientStrategy;
import com.itmo.mrdvd.proxy.strategies.LocalStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.service.AbstractExecutor;
import com.itmo.mrdvd.service.AbstractSender;

public class ClientProxy extends AbstractProxy {
  public ClientProxy(AbstractSender<Query, String, Response> sender, AbstractExecutor exec) {
    this(sender, exec, new HashMap<>());
  }

  public ClientProxy(AbstractSender<Query, String, Response> sender, AbstractExecutor exec, Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(new ClientStrategy(sender));
    ProxyStrategy local = new LocalStrategy(exec);
    setStrategy("help", local);
    setStrategy("exit", local);
  }
}
