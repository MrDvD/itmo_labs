package com.itmo.mrdvd.builder.builders;

import com.itmo.mrdvd.builder.interactors.Interactor;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface InteractiveBuilder<T> extends Builder<T> {
  public <U> void addInteractiveBuilder(BiConsumer<T, U> setter, InteractiveBuilder<U> builder)
      throws IllegalArgumentException;

  public <U> void addInteractiveSetter(
      BiConsumer<T, U> setter, Interactor<?> inter, Predicate<U> validator)
      throws IllegalArgumentException;
}
