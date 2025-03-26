package com.itmo.mrdvd.builder.builders;

import java.util.Optional;
import java.util.function.BiConsumer;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.input.InputDevice;

public interface InteractiveBuilder<T, K extends InputDevice> extends Builder<T> {
  public <U> InteractiveBuilder<T, K> addInteractiveBuilder(
      InteractiveBuilder<U, ?> builder,
      BiConsumer<T, U> setter,
      Class<U> valueCls,
      TypedPredicate<U> validator)
      throws IllegalArgumentException;

  public <U> InteractiveBuilder<T, K> addInteractiveSetter(
      BiConsumer<T, U> setter, Class<U> valueCls, Interactor<?, K> inter, TypedPredicate<U> validator)
      throws IllegalArgumentException;
  
  public InteractiveBuilder<T, K> setIn(K in);

  public Optional<K> getIn();
}
