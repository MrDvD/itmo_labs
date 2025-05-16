package com.itmo.mrdvd.proxy;

import com.itmo.mrdvd.proxy.serviceQuery.AbstractServiceQuery;
import com.itmo.mrdvd.proxy.serviceQuery.ServiceQuery;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
  public Optional<ServiceQuery> processQuery(ServiceQuery q) {
    try {
      if (this.strats.containsKey(q.getName())) {
        return this.strats.get(q.getName()).make(q);
      }
      if (this.defaultStrat == null) {
        throw new IllegalStateException("Не установлена прокси-стратегия по умолчанию.");
      }
      return this.defaultStrat.make(q);
    } catch (RuntimeException e) {
      return Optional.of(new AbstractServiceQuery("error", List.of(e.getLocalizedMessage())) {});
    }
  }
}
