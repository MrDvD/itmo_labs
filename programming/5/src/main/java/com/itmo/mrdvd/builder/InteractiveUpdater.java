package com.itmo.mrdvd.builder;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.itmo.mrdvd.builder.functionals.TypedPredicate;

public interface InteractiveUpdater<T> extends Updater<T>, InteractiveBuilder<T> {
   public <U> InteractiveUpdater<T> addInteractiveUpdater(InteractiveUpdater<U> updater, BiConsumer<T, U> setter, Function<T,U> getter, Class<U> valueCls, TypedPredicate<U> validator) throws IllegalArgumentException;
   public <U> InteractiveUpdater<T> addInteractiveSetter(BiConsumer<T, U> setter, Function<T,U> getter, Class<U> valueCls, Interactor<?> inter, TypedPredicate<U> validator) throws IllegalArgumentException;
}
