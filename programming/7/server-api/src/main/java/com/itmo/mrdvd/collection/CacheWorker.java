package com.itmo.mrdvd.collection;

public interface CacheWorker<T, V extends java.util.Collection<? extends T>, K>
    extends CrudWorker<T, V, K> {
  public void setCache(V cache);

  public void clearCache();
}
