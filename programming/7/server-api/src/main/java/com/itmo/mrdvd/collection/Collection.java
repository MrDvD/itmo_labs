package com.itmo.mrdvd.collection;

public abstract class Collection<T, V extends java.util.Collection<? extends T>>
    implements CacheWorker<T, V, Long>, Iterable<T> {
  public abstract CollectionMetadata getMetadata();

  public abstract void setMetadata(CollectionMetadata metadata);
}
