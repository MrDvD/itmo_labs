package com.itmo.mrdvd.service.shell.query_fill_strategy;

import com.itmo.mrdvd.proxy.Query;
import java.io.IOException;

/** A strategy for Shell to fill QueryDTO with args. */
public interface QueryFillStrategy {
  public Query fillArgs(Query q) throws IOException;
}
