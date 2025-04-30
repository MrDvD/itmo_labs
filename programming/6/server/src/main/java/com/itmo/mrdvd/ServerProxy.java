package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.strategies.InformStrategy;
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
    setStrategy("clear", new InformStrategy(exec, "Коллекция очищена."));
    setStrategy("remove_last", new InformStrategy(exec, "Последний элемент удалён."));
    setStrategy("remove_at", new InformStrategy(exec, "Элемент удалён."));
    setStrategy("remove_by_id", new InformStrategy(exec, "Элемент удалён."));
  }
}
