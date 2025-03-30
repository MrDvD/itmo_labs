package com.itmo.mrdvd.builder.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.ProcessStatus;

public class ObjectBuilder<T> implements Builder<T> {
  protected final List<BiConsumer> setters;
  protected final List<Object> objects;
  protected final List<Supplier<?>> methods;
  protected final List<Predicate> validators;
  protected Supplier<T> newMethod;
  protected T rawObject;

  public ObjectBuilder() {
    this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public ObjectBuilder(
      List<BiConsumer> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<Predicate> validators) {
    this.setters = setters;
    this.objects = objects;
    this.methods = methods;
    this.validators = validators;
  }

  @Override
  public ObjectBuilder<T> of(Supplier<T> newMethod) {
    this.newMethod = newMethod;
    return this;
  }

  public <U> ObjectBuilder<T> set(BiConsumer<T, U> setter, Object value, Class<U> valueCls) {
    return set(setter, value, valueCls, null);
  }

  @Override
  public <U> ObjectBuilder<T> set(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls, Predicate<U> validator) {
    if (setter == null) {
      throw new IllegalArgumentException("Setter не может быть null.");
    }
    setters.add(setter);
    objects.add(value);
    methods.add(null);
    validators.add(validator);
    return this;
  }

  public <U> ObjectBuilder<T> setFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls) {
    return setFromMethod(setter, method, valueCls, null);
  }

  @Override
  public <U> ObjectBuilder<T> setFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls, Predicate<U> validator) {
    if (setter == null) {
      throw new IllegalArgumentException("Setter не может быть null.");
    }
    setters.add(setter);
    objects.add(null);
    methods.add(method);
    validators.add(validator);
    return this;
  }

  protected ProcessStatus processSetter(int index) {
    if (methods.get(index) != null) {
      objects.set(index, methods.get(index).get());
    }
    if (validators.get(index) != null && !validators.get(index).test(objects.get(index))) {
      return ProcessStatus.FAILURE;
    }
    setters.get(index).accept(rawObject, objects.get(index));
    return ProcessStatus.SUCCESS;
  }

  protected Optional<T> getObject() {
    for (int i = 0; i < setters.size(); i++) {
      if (processSetter(i).equals(ProcessStatus.FAILURE)) {
        return Optional.empty();
      }
    }
    return Optional.of(rawObject);
  }

  @Override
  public Optional<T> build() throws IllegalArgumentException {
    if (newMethod == null) {
      throw new IllegalArgumentException("Метод создания объекта не может быть null.");
    }
    this.rawObject = newMethod.get();
    return getObject();
  }
}
