package com.itmo.mrdvd.proxy.response;

public class OkayResponse extends AbstractResponse {
  public OkayResponse(Object body) {
    super("okay", body);
  }
}
