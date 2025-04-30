package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class ServerProxy extends AbstractProxy {
  public ServerProxy(AbstractExecutor exec) {
    this(exec, new HashMap<>());
  }

  public ServerProxy(AbstractExecutor exec, Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(new WrapStrategy(exec));
  }
}
