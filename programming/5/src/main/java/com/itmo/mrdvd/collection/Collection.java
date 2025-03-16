package com.itmo.mrdvd.collection;

public abstract class Collection<T, U> implements CollectionWorker<T, U>, Iterable<T>  {
   public abstract CollectionMetadata getMetadata();
}
