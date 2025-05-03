package com.itmo.mrdvd.builder.builders;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface SuppliedBuilder<T> extends Builder<T> {
  public SuppliedBuilder<T> withElements(List<?> elems);

  public <U> SuppliedBuilder<T> addSuppliedBuilder(
      BiConsumer<T, U> setter, SuppliedBuilder<U> builder) throws IllegalArgumentException;

  public <U> SuppliedBuilder<T> setFromSupplies(BiConsumer<T, U> setter)
      throws IllegalArgumentException;

  public <U> SuppliedBuilder<T> setFromSupplies(BiConsumer<T, U> setter, Predicate<U> validator)
      throws IllegalArgumentException;
}
