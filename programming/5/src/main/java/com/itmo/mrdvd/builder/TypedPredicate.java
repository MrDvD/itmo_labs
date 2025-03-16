package com.itmo.mrdvd.builder;

import java.util.function.Predicate;

@FunctionalInterface
public interface TypedPredicate<T> extends Predicate<T> {
   public default boolean testRaw(Object obj) {
      return false;
   };

   public static <T> TypedPredicate<T> of(Class<T> type, Predicate<T> consumer) {
      return new TypedPredicate<T>() {
         @Override
         public boolean testRaw(Object obj) {
            if (type.isInstance(obj)) {
               return consumer.test(type.cast(obj));
            } else {
               throw new IllegalArgumentException("Invalid input type.");
            }
         }

         @Override
         public boolean test(T obj) {
            return consumer.test(obj);
         }
      };
   }
}
