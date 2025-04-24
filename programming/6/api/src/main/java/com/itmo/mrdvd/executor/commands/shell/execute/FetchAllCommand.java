package com.itmo.mrdvd.executor.commands.shell.execute;

import com.itmo.mrdvd.executor.commands.CommandWithParams;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.shell.Shell;
import java.util.List;

/**
 * Fetches the info about queries from the Proxy server and caches it. Also gets the JavaScript
 * files for params validation.
 *
 * <p>Should be launched either upon Shell start or as independent command.
 *
 * @param List server's response
 * @param Shell shell to work with
 */
public class FetchAllCommand implements CommandWithParams {
  protected List<?> params;

  /** Passes the response, shell & client proxy into the command. */
  @Override
  public FetchAllCommand withParams(List<?> params) {
    this.params = params;
    return this;
  }

  @Override
  public void execute() {
    if (this.params == null || this.params.isEmpty() || !(this.params.get(0) instanceof Shell)) {
      throw new IllegalStateException("Не предоставлен интерпретатор для исполнения команды.");
    }
    if (this.params.size() == 1 || !(this.params.get(1) instanceof List)) {
      throw new IllegalStateException("Не предоставлен список запросов для добавления.");
    }
    Shell sh = (Shell) this.params.get(0);
    try {
      List<Query> arr = (List) this.params.get(1);
      for (Query q : arr) {
        sh.setQuery(q);
      }
    } catch (ClassCastException e) {
      throw new RuntimeException("Не удалось добавить переданные запросы.");
    }
  }

  @Override
  public String name() {
    return "fetch_all";
  }
}
