package com.itmo.mrdvd.builder.updaters;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.input.InputDevice;

public interface InteractiveUpdater<T, K extends InputDevice> extends Updater<T> {
  public <U> InteractiveUpdater<T, K> addInteractiveUpdater(
      InteractiveUpdater<U, ?> updater,
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      TypedPredicate<U> validator)
      throws IllegalArgumentException;

  public <U> InteractiveUpdater<T, K> addInteractiveChange(
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      Interactor<?, K> inter,
      TypedPredicate<U> validator)
      throws IllegalArgumentException;

  public InteractiveUpdater<T, K> setIn(K in);

  public Optional<K> getIn();
}
