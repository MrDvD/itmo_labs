package com.itmo.mrdvd.collection;

public interface IdGenerator {
   public Long getId(Object obj);
   public int takeId(Long id);
   public int freeId(Long id);
   public boolean isTaken(Long id);
}
