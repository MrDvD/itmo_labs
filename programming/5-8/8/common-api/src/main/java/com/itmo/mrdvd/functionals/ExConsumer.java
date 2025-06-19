package com.itmo.mrdvd.functionals;

@FunctionalInterface
public interface ExConsumer<T, E extends Exception> {
  public void accept(T t) throws E;
}
