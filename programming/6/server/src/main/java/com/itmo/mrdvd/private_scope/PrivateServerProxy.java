package com.itmo.mrdvd.private_scope;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.strategies.FetchAllStrategy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.RedirectToStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class PrivateServerProxy extends AbstractProxy {
  public PrivateServerProxy(AbstractExecutor exec, Proxy other) {
    this(exec, other, new HashMap<>());
  }

  public PrivateServerProxy(AbstractExecutor exec, Proxy other, Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(new RedirectToStrategy(other));
    setStrategy("fetch_all", new FetchAllStrategy(exec, other));
    setStrategy("save", new InformStrategy(exec, "Коллекция сохранена."));
    setStrategy("load", new InformStrategy(exec, "Коллекция загружена."));
    // shutdown
  }
}
