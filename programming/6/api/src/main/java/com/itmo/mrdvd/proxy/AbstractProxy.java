package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import java.util.Map;

public abstract class AbstractProxy implements Proxy {
  protected Map<String, ProxyStrategy> strats;
  protected ProxyStrategy defaultStrat;

  public AbstractProxy(Map<String, ProxyStrategy> strats) {
    this.strats = strats;
  }

  @Override
  public void setDefaultStrategy(ProxyStrategy s) {
    this.defaultStrat = s;
  }

  @Override
  public void setStrategy(String name, ProxyStrategy s) {
    this.strats.put(name, s);
  }

  @Override
  public Response processQuery(Query q) {
    if (this.strats.containsKey(q.getCmd())) {
      return this.strats.get(q.getCmd()).make(q);
    }
    if (this.defaultStrat == null) {
      throw new IllegalStateException("Не установлена прокси-стратегия по умолчанию.");
    }
    return this.defaultStrat.make(q);
  }
}
