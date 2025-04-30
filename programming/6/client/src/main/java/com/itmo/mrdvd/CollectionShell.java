package com.itmo.mrdvd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.service.shell.DefaultShell;
import com.itmo.mrdvd.service.shell.query_fill_strategy.ConnectQueryStrategy;
import com.itmo.mrdvd.service.shell.query_fill_strategy.ExitQueryStrategy;
import com.itmo.mrdvd.service.shell.query_fill_strategy.QueryFillStrategy;
import com.itmo.mrdvd.service.shell.query_fill_strategy.ReadLongQueryStrategy;
import com.itmo.mrdvd.service.shell.query_fill_strategy.RemoveAtQueryStrategy;
import com.itmo.mrdvd.service.shell.response_strategy.PrintStrategy;
import com.itmo.mrdvd.service.shell.response_strategy.ShellResponseStrategy;

public class CollectionShell extends DefaultShell {
  public CollectionShell(Proxy proxy, Function<String, Query> query) {
    this(proxy, query, new ArrayList<>(), new HashMap<>(), new HashMap<>());
  }

  public CollectionShell(Proxy proxy, Function<String, Query> query, List<TTY> tty, Map<String, QueryFillStrategy> args, Map<String, ShellResponseStrategy> strats) {
    super(proxy, query, tty, args, strats);
    setDefaultResponseStrategy(new PrintStrategy(this));
    setQueryStrategy("exit", new ExitQueryStrategy(this));
    setQueryStrategy("connect", new ConnectQueryStrategy(this));
    setQueryStrategy("remove_at", new RemoveAtQueryStrategy(this));
    setQueryStrategy("remove_by_id", new ReadLongQueryStrategy(this));
    setQueryStrategy("count_greater_than_event", new ReadLongQueryStrategy(this));
  }
}
