package com.itmo.mrdvd.builder;

import java.util.Optional;
import java.util.function.Function;

public interface ObjectBuilder<T> {
   public void addSetter(Function<Void, Integer> func);
   public Optional<T> build();
}