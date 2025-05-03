package com.itmo.mrdvd.proxy.mappers;

import com.itmo.mrdvd.proxy.Query;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class QueryMapper implements Mapper<Map<String, String>, Query> {
  private final Supplier<Query> supplier;

  public QueryMapper(Supplier<Query> supplier) {
    this.supplier = supplier;
  }

  @Override
  public Query wrap(Map<String, String> obj) {
    Query q = this.supplier.get();
    q.setCmd(obj.get("cmd"));
    q.setSignature(obj.get("signature"));
    q.setDesc(obj.get("desc"));
    return q;
  }

  @Override
  public Optional<Map<String, String>> unwrap(Query q) {
    return Optional.of(
        Map.of("cmd", q.getCmd(), "signature", q.getSignature(), "desc", q.getDesc()));
  }
}
