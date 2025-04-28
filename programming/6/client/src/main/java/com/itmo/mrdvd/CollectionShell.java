package com.itmo.mrdvd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.service.DefaultShell;
import com.itmo.mrdvd.service.shell_response_strategy.PrintStrategy;
import com.itmo.mrdvd.service.shell_response_strategy.ShellResponseStrategy;

public class CollectionShell extends DefaultShell {
  public CollectionShell(Proxy proxy, Function<String, Query> query) {
    this(proxy, query, new ArrayList<>(), new HashMap<>(), new HashMap<>());
  }

  public CollectionShell(Proxy proxy, Function<String, Query> query, List<TTY> tty, Map<String, Object> args, Map<String, ShellResponseStrategy> strats) {
    super(proxy, query, tty, args, strats);
    setDefaultResponseStrategy(new PrintStrategy(this));
  }
}
