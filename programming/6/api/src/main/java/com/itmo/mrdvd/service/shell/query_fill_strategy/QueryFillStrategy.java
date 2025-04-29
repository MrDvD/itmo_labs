package com.itmo.mrdvd.service.shell.query_fill_strategy;

import java.io.IOException;

import com.itmo.mrdvd.executor.queries.Query;

/**
 * A strategy for Shell to fill QueryDTO with args.
 */
public interface QueryFillStrategy {
  public Query fillArgs(Query q) throws IOException;
}
