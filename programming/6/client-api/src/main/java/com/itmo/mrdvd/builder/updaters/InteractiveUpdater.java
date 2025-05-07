package com.itmo.mrdvd.builder.updaters;

import com.itmo.mrdvd.builder.interactors.Interactor;
import com.itmo.mrdvd.device.input.InputDevice;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InteractiveUpdater<T, K extends InputDevice> extends Updater<T> {
  public <U> InteractiveUpdater<T, K> addInteractiveUpdater(
      InteractiveUpdater<U, ?> updater,
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      Predicate<U> validator)
      throws IllegalArgumentException;

  public <U> InteractiveUpdater<T, K> addInteractiveChange(
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      Interactor<?> inter,
      Predicate<U> validator)
      throws IllegalArgumentException;

  public InteractiveUpdater<T, K> setIn(K in);

  public Optional<K> getIn();
}
