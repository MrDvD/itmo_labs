package com.itmo.mrdvd.proxy.mappers;

public interface VariableMapper<T, U, V, W> extends Mapper<T, U> {
  public void setStrategy(String name, Mapper<V, W> strat);
}
