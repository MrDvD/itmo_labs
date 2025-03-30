package com.itmo.mrdvd.builder.updaters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.ProcessStatus;

public class ObjectUpdater<T> implements Updater<T> {
  protected final List<BiConsumer> setters;
  protected final List<Object> objects;
  protected final List<Supplier<?>> methods;
  protected final List<Predicate> validators;
  protected Supplier<T> newMethod;
  protected T rawObject;

  public ObjectUpdater() {
    this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public ObjectUpdater(
      List<BiConsumer> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<Predicate> validators) {
    this.setters = setters;
    this.objects = objects;
    this.methods = methods;
    this.validators = validators;
  }

  public <U> ObjectUpdater<T> change(BiConsumer<T, U> setter, Object value, Class<U> valueCls) {
    return change(setter, value, valueCls, null);
  }

  @Override
  public <U> ObjectUpdater<T> change(
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

  public <U> ObjectUpdater<T> changeFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls) {
    return changeFromMethod(setter, method, valueCls, null);
  }

  @Override
  public <U> ObjectUpdater<T> changeFromMethod(
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

  protected ProcessStatus processChange(int index) throws RuntimeException {
    if (methods.get(index) != null) {
      objects.set(index, methods.get(index).get());
    }
    if (validators.get(index) != null && !validators.get(index).test(objects.get(index))) {
      return ProcessStatus.FAILURE;
    }
    setters.get(index).accept(rawObject, objects.get(index));
    return ProcessStatus.SUCCESS;
  }

  protected Optional<T> getObject() throws RuntimeException {
    for (int i = 0; i < setters.size(); i++) {
      if (processChange(i).equals(ProcessStatus.FAILURE)) {
        return Optional.empty();
      }
    }
    return Optional.of(rawObject);
  }

  @Override
  public Optional<T> update(T rawObject) throws IllegalArgumentException, RuntimeException {
    if (rawObject == null) {
      throw new IllegalArgumentException("Объект не может быть null.");
    }
    this.rawObject = rawObject;
    return getObject();
  }
}
