package com.itmo.mrdvd.proxy.strategies;

import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.response.EmptyResponse;
import com.itmo.mrdvd.proxy.response.Response;
import com.itmo.mrdvd.service.AbstractSender;
import java.io.IOException;
import java.util.Optional;

public class SendServerStrategy implements ProxyStrategy {
  private final AbstractSender<Query, String, Response> sender;

  public SendServerStrategy(AbstractSender<Query, String, Response> sender) {
    this.sender = sender;
  }

  @Override
  public Response make(Query q) {
    try {
      this.sender.connect();
      Optional<? extends Response> r = this.sender.send(q);
      if (r.isPresent()) {
        return r.get();
      }
      return new EmptyResponse();
    } catch (IOException e) {
      throw new RuntimeException("Не удалось отправить запрос.");
    }
  }
}
