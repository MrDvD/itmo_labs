package com.itmo.mrdvd.collection;

public interface CollectionWorker<T> {
   public int add(T obj);
   public int update(Long id, T obj);
   public int remove(Long id);
   public void clear();
}
