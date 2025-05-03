package com.itmo.mrdvd.proxy.response;

import java.util.List;

public class OkayResponse extends AbstractResponse {
  public OkayResponse(List<Object> body) {
    super("okay", body);
  }
}
