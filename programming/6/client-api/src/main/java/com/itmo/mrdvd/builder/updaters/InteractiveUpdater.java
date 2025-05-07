package com.itmo.mrdvd.builder.updaters;

import com.itmo.mrdvd.builder.interactors.Interactor;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InteractiveUpdater<T> extends Updater<T> {
  public <U> void addInteractiveUpdater(
      BiConsumer<T, U> setter, Function<T, U> getter, InteractiveUpdater<U> updater)
      throws IllegalArgumentException;

  public <U> void addInteractiveChange(
      BiConsumer<T, U> setter, Function<T, U> getter, Interactor<?> inter, Predicate<U> validator)
      throws IllegalArgumentException;
}
