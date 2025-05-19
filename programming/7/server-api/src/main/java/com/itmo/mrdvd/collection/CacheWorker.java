package com.itmo.mrdvd.collection;

public interface CacheWorker<T, V extends java.util.Collection<? extends T>>
    extends CrudWorker<T, V> {
  public void setCache(V cache);

  public void clearCache();
}
