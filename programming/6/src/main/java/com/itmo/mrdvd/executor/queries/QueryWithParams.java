package com.itmo.mrdvd.executor.queries;

import java.util.List;

public abstract class QueryWithParams extends Query {
  private List<String> params;

  public QueryWithParams(String name, String signature, String desc, List<String> params) {
    super(name, signature, desc);
    this.params = params;
  }

  /** Just setter for deserialization purposes. */
  public void setParams(List<String> params) {
    this.params = params;
  }

  public List<String> getParams() {
    return this.params;
  }
}
