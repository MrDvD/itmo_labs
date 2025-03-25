package com.itmo.mrdvd.builder.functionals;

import java.util.function.Predicate;

@FunctionalInterface
public interface TypedPredicate<T> extends Predicate<T> {
  public default boolean testRaw(Object obj) {
    return false;
  }
  ;

  public static <T> TypedPredicate<T> of(Class<T> type, Predicate<T> consumer)
      throws IllegalArgumentException {
    return new TypedPredicate<T>() {
      @Override
      public boolean testRaw(Object obj) {
        if (consumer == null) {
          throw new IllegalArgumentException("Consumer не может быть null.");
        }
        if (type == null) {
          throw new IllegalArgumentException("Type не может быть null.");
        }
        if (type.isInstance(obj)) {
          return consumer.test(type.cast(obj));
        } else {
          throw new IllegalArgumentException(
              String.format(
                  "Объект %s не соответствует переданному классу.",
                  obj.getClass().getCanonicalName()));
        }
      }

      @Override
      public boolean test(T obj) {
        return consumer.test(obj);
      }
    };
  }
}
