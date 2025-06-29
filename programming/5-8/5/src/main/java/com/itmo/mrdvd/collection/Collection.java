package com.itmo.mrdvd.collection;

import com.itmo.mrdvd.object.HavingId;

public abstract class Collection<T extends HavingId, U>
    implements CollectionWorker<T, U>, Iterable<T> {
  public abstract CollectionMetadata getMetadata();

  public abstract void setMetadata(CollectionMetadata metadata);
}
