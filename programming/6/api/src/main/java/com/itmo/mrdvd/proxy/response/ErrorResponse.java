package com.itmo.mrdvd.proxy.response;

import java.util.List;

public class ErrorResponse extends AbstractResponse {
  public ErrorResponse(String error) {
    super("error", List.of(error));
  }
}
