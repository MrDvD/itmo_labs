package com.itmo.mrdvd.collection;

import java.util.function.Predicate;

public interface CachedCrudWorker<T, V extends java.util.Collection<? extends T>, K>
    extends CrudWorker<T, V, K>, Iterable<T> {
  public void setCache(V cache);

  public void clearCache();

  /** Removes an object if some condition is met. */
  public void remove(K id, Predicate<T> cond);
}
