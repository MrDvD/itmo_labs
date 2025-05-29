package com.itmo.mrdvd.collection;

public interface CachedCrudWorker<T, V extends java.util.Collection<? extends T>, K>
    extends CrudWorker<T, V, K>, Iterable<T> {
  public void setCache(V cache);

  public void clearCache();
}
