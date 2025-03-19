package com.itmo.mrdvd.builder;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public interface Builder<T> {
   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls, Predicate<U> validator);
   public Optional<T> build();
}
