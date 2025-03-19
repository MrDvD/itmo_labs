package com.itmo.mrdvd.builder;

import java.util.Optional;

public interface Updater<T> extends Builder<T> {
   public Optional<T> update(T rawObject) throws IllegalArgumentException;
}
