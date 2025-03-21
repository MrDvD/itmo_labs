package com.itmo.mrdvd.builder.builders;

import java.util.function.BiConsumer;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;

public interface InteractiveBuilder<T> extends Builder<T> {
   public <U> InteractiveBuilder<T> addInteractiveBuilder(InteractiveBuilder<U> builder, BiConsumer<T, U> setter, Class<U> valueCls, TypedPredicate<U> validator) throws IllegalArgumentException;
   public <U> InteractiveObjectBuilder<T> addInteractiveSetter(BiConsumer<T, U> setter, Class<U> valueCls, Interactor<?> inter, TypedPredicate<U> validator) throws IllegalArgumentException;
}
