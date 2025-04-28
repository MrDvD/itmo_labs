package com.itmo.mrdvd.proxy.response;

public class InternalErrorResponse extends AbstractResponse {
  public InternalErrorResponse(String body) {
    super("internal_error", body);
  }
}
