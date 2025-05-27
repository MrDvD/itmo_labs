package com.itmo.mrdvd.validators;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Validator<T> {
  public <U> Validator<T> check(Function<T, U> getter, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException;

  public <U> Validator<T> check(Function<T, U> getter, Validator<U> validator)
      throws IllegalArgumentException;

  public boolean validate(T obj);
}
