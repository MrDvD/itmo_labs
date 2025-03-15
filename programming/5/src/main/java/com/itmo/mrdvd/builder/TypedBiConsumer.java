package com.itmo.mrdvd.builder;

import java.util.function.BiConsumer;

public class TypedBiConsumer<T,U> {
   private final Class<U> type;
   private final BiConsumer<T,U> consumer;

   public TypedBiConsumer(Class<U> type, BiConsumer<T,U> consumer) {
       this.type = type;
       this.consumer = consumer;
   }

   public void accept(T obj1, Object obj2) {
       if (type.isInstance(obj2)) {
           consumer.accept(obj1, type.cast(obj2));
       } else {
           throw new IllegalArgumentException("Invalid input type.");
       }
   }

   public Class<U> getType() {
       return type;
   }
}