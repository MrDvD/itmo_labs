package com.itmo.mrdvd;

import java.util.HashMap;
import java.util.Map;

import com.itmo.mrdvd.proxy.AbstractProxy;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.Mapper;
import com.itmo.mrdvd.proxy.mappers.QueryMapper;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.proxy.strategies.CacheQueriesStrategy;
import com.itmo.mrdvd.proxy.strategies.ConnectStrategy;
import com.itmo.mrdvd.proxy.strategies.ExitStrategy;
import com.itmo.mrdvd.proxy.strategies.ProxyStrategy;
import com.itmo.mrdvd.proxy.strategies.SendServerStrategy;
import com.itmo.mrdvd.proxy.strategies.WrapStrategy;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class ClientProxy extends AbstractProxy {
  public ClientProxy(AbstractSender<Query, String, Response> sender, AbstractExecutor exec, QueryMapper mapper) {
    this(sender, exec, mapper, new HashMap<>());
  }

  public ClientProxy(AbstractSender<Query, String, Response> sender, AbstractExecutor exec, Mapper<Map<String, String>, Query> mapper, Map<String, ProxyStrategy> strats) {
    super(strats);
    setDefaultStrategy(new SendServerStrategy(sender));
    setStrategy("help", new WrapStrategy(exec));
    setStrategy("exit", new ExitStrategy(exec));
    setStrategy("connect", new ConnectStrategy(exec));
    setStrategy("fetch_all", new CacheQueriesStrategy(sender, exec, mapper));
  }
}
