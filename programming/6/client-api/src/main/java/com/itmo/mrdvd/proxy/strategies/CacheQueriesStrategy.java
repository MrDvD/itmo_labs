package com.itmo.mrdvd.proxy.strategies;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.response.AbstractResponse;
import com.itmo.mrdvd.proxy.response.EmptyResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractSender;
import com.itmo.mrdvd.service.executor.AbstractExecutor;

public class CacheQueriesStrategy implements ProxyStrategy {
  private final AbstractSender<Query, String, Response> sender;
  private final AbstractExecutor exec;

  public CacheQueriesStrategy(
      AbstractSender<Query, String, Response> sender,
      AbstractExecutor exec) {
    this.sender = sender;
    this.exec = exec;
  }

  @Override
  public Response make(Query q) throws IllegalStateException {
    if (this.exec != null) {
      try {
        this.sender.connect();
        Optional<? extends Response> r = this.sender.send(q);
        if (r.isPresent()) {
          this.exec.clearCachedQueries();
          for (Object qq : (List) r.get().getBody()) {
            this.exec.setQuery((Query) qq);
          }
          return new AbstractResponse(q.getCmd(), List.of("Получен набор запросов от сервера.")) {};
        }
        return new EmptyResponse();
      } catch (ClassCastException e) {
        throw new RuntimeException("Не удалось распознать полученные запросы.");
      } catch (IOException e) {
        throw new RuntimeException("Не удалось отправить запрос.");
      }

    } else {
      throw new IllegalStateException("Не передан исполнитель для кеширования запросов.");
    }
  }
}
