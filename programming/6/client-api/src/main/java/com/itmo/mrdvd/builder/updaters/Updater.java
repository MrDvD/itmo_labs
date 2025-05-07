package com.itmo.mrdvd.builder.updaters;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Updater<T> {
  public <U> void change(BiConsumer<T, U> setter, Supplier<U> method);

  public <U> void change(BiConsumer<T, U> setter, Supplier<U> method, Predicate<U> validator);

  public Optional<T> update(T rawObject) throws IllegalArgumentException;
}
