package com.itmo.mrdvd.builder.builders;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Builder<T> {
  public Builder<T> of(Supplier<T> newMethod);

  public <U> Builder<T> set(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls, Predicate<U> validator);

  public <U> Builder<T> setFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls, Predicate<U> validator);

  public Optional<T> build() throws IllegalArgumentException;
}
