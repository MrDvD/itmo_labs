package com.itmo.mrdvd.builder.updaters;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Updater<T> {
   public <U> Updater<T> change(BiConsumer<T,U> setter, Object value, Class<U> valueCls, Predicate<U> validator);
   public <U> Updater<T> changeFromMethod(BiConsumer<T,U> setter, Supplier<U> method, Class<U> valueCls, Predicate<U> validator);
   public Optional<T> update(T rawObject) throws IllegalArgumentException;
}
