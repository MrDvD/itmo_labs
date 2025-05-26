package com.itmo.mrdvd.functionals;

@FunctionalInterface
public interface ExSupplier<T, E extends Exception> {
  public T get() throws E;
}
