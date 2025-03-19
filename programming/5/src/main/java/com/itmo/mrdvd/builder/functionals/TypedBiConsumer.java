package com.itmo.mrdvd.builder.functionals;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface TypedBiConsumer<T, U> extends BiConsumer<T, U> {
   public default void acceptRaw(T obj1, Object obj2) {};

   public static <T, U> TypedBiConsumer<T, U> of(Class<U> type, BiConsumer<T, U> consumer) {
      return new TypedBiConsumer<T, U>() {
         @Override
         public void acceptRaw(T obj1, Object obj2) throws IllegalArgumentException {
            if (consumer == null) {
               throw new IllegalArgumentException("Consumer не может быть null.");
            }
            if (type == null) {
               throw new IllegalArgumentException("Type не может быть null.");
            }
            if (type.isInstance(obj2)) {
               consumer.accept(obj1, type.cast(obj2));
            } else {
               throw new IllegalArgumentException("Объект не соответствует переданному типу.");
            }
         }

         @Override
         public void accept(T obj1, U obj2) {
            consumer.accept(obj1, obj2);
         }
      };
   }
}