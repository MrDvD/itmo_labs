package com.itmo.mrdvd.builder.updaters;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.input.InputDevice;

public interface InteractiveUpdater<T> extends Updater<T> {
  public <U> InteractiveUpdater<T> addInteractiveUpdater(
      InteractiveUpdater<U> updater,
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      TypedPredicate<U> validator)
      throws IllegalArgumentException;

  public <U> InteractiveUpdater<T> addInteractiveChange(
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      Interactor<?> inter,
      TypedPredicate<U> validator)
      throws IllegalArgumentException;
  
  public InteractiveUpdater<T> setIn(InputDevice in);

  public Optional<InputDevice> getIn();
}
