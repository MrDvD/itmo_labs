package com.itmo.mrdvd.proxy.strategies;

import java.io.IOException;
import java.util.Optional;

import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.response.EmptyResponse;
import com.itmo.mrdvd.proxy.response.InternalErrorResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractSender;

public class SendServerStrategy implements ProxyStrategy {
  private final AbstractSender<Query, String, Response> sender;

  public SendServerStrategy(AbstractSender<Query, String, Response> sender) {
    this.sender = sender;
  }

  @Override
  public Response make(Query q) {
    try {
      Optional<Response> r = this.sender.send(q);
      if (r.isPresent()) {
        return r.get();
      }
      return new EmptyResponse();
    } catch (IOException e) {
      return new InternalErrorResponse("не удалось отправить запрос");
    }
  }
}
