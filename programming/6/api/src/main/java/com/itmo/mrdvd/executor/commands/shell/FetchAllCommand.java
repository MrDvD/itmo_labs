package com.itmo.mrdvd.executor.commands.shell;

import java.util.List;

import com.itmo.mrdvd.executor.commands.Command;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.service.AbstractExecutor;

/**
 * Fetches the info about queries from the Proxy server and caches it. Also gets the JavaScript
 * files for params validation.
 *
 * <p>Should be launched either upon Shell start or as independent command.
 *
 * @param List server's response
 * @param TTY shell to work with
 */
public class FetchAllCommand implements Command<Void> {
  private final AbstractExecutor exec;

  public FetchAllCommand(AbstractExecutor exec) {
    this.exec = exec;
  }

  @Override
  public Void execute(List<Object> params) {
    try {
      for (Object q : params) {
        this.exec.setQuery((Query) q);
      }
    } catch (ClassCastException e) {
      throw new RuntimeException("Не удалось распознать полученные запросы.");
    }
    return null;
  }

  @Override
  public String name() {
    return "fetch_all";
  }

  @Override
  public String signature() {
    return name();
  }

  @Override
  public String description() {
    return "запросить у сервера доступные запросы";
  }
}
