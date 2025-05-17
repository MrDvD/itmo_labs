package com.itmo.mrdvd;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.Proxy;
import com.itmo.mrdvd.service.shell.DefaultShell;
import com.itmo.mrdvd.service.shell.queryFillStrategy.ConnectQueryStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.QueryFillStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.ReadIntQueryStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.ReadLongQueryStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.ReadObjectStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.ReadStringQueryStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.ShellQueryStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.SkipLineStrategy;
import com.itmo.mrdvd.service.shell.queryFillStrategy.UpdateObjectStrategy;
import com.itmo.mrdvd.service.shell.responseStrategy.PrintStrategy;
import com.itmo.mrdvd.service.shell.responseStrategy.ShellResponseStrategy;
import com.itmo.mrdvd.service.shell.responseStrategy.ShutdownStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionShell extends DefaultShell {
  public CollectionShell(Proxy proxy) {
    this(proxy, new ArrayList<>(), new HashMap<>(), new HashMap<>(), new HashSet<>());
  }

  public CollectionShell(
      Proxy proxy,
      List<TTY> tty,
      Map<String, QueryFillStrategy> args,
      Map<String, ShellResponseStrategy> strats,
      Set<String> usedTtys) {
    super(proxy, tty, args, strats, usedTtys);
    setDefaultResponseStrategy(new PrintStrategy(this));
    setResponseStrategy("shutdown", new ShutdownStrategy(this, new PrintStrategy(this)));
    setQueryStrategy("exit", new ShellQueryStrategy(this));
    setQueryStrategy(
        "execute_script",
        new SkipLineStrategy(
            this, new ReadStringQueryStrategy(this, new ShellQueryStrategy(this))));
    setQueryStrategy("connect", new SkipLineStrategy(this, new ConnectQueryStrategy(this)));
    setQueryStrategy("remove_at", new SkipLineStrategy(this, new ReadIntQueryStrategy(this)));
    setQueryStrategy("remove_by_id", new SkipLineStrategy(this, new ReadLongQueryStrategy(this)));
    setQueryStrategy(
        "count_greater_than_event", new SkipLineStrategy(this, new ReadLongQueryStrategy(this)));
  }

  public void setBuilders(InteractiveBuilder<?> builder) {
    setQueryStrategy("add", new ReadObjectStrategy(builder, new SkipLineStrategy(this)));
    setQueryStrategy("add_if_max", new ReadObjectStrategy(builder, new SkipLineStrategy(this)));
    setQueryStrategy("update", new UpdateObjectStrategy<>(this, builder));
  }
}
