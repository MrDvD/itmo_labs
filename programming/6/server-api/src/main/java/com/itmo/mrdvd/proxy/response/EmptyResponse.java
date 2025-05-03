package com.itmo.mrdvd.proxy.response;

import java.util.List;

public class EmptyResponse extends AbstractResponse {
  public EmptyResponse() {
    super("empty", List.of("Нет ответа."));
  }
}
