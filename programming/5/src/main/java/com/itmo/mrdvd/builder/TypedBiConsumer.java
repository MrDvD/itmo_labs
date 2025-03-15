package com.itmo.mrdvd.builder;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface TypedBiConsumer<T, U> extends BiConsumer<T, U> {
   public default void acceptRaw(T obj1, Object obj2) {};
   
   public default Class<U> getType() {
      return null;
   };

   public static <T, U> TypedBiConsumer<T, U> of(Class<U> type, BiConsumer<T, U> consumer) {
      return new TypedBiConsumer<T, U>() {
         @Override
         public void acceptRaw(T obj1, Object obj2) {
            if (type.isInstance(obj2)) {
               consumer.accept(obj1, type.cast(obj2));
            } else {
               throw new IllegalArgumentException("Invalid input type.");
            }
         }

         @Override
         public void accept(T obj1, U obj2) {
            consumer.accept(obj1, obj2);
         }

         @Override
         public Class<U> getType() {
            return type;
         }
      };
   }
}