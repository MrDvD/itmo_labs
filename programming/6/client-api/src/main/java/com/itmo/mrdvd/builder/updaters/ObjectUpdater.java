package com.itmo.mrdvd.builder.updaters;

import com.itmo.mrdvd.builder.ProcessStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ObjectUpdater<T> implements Updater<T> {
  protected final List<BiConsumer> setters;
  protected final List<Supplier<?>> methods;
  protected final List<Predicate> validators;
  protected Supplier<T> newMethod;
  protected T rawObject;

  public ObjectUpdater() {
    this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public ObjectUpdater(
      List<BiConsumer> setters, List<Supplier<?>> methods, List<Predicate> validators) {
    this.setters = setters;
    this.methods = methods;
    this.validators = validators;
  }

  public <U> void change(BiConsumer<T, U> setter, Supplier<U> method) {
    change(setter, method, null);
  }

  @Override
  public <U> void change(BiConsumer<T, U> setter, Supplier<U> method, Predicate<U> validator) {
    if (setter == null) {
      throw new IllegalArgumentException("Не предоставлен сеттер.");
    }
    setters.add(setter);
    methods.add(method);
    validators.add(validator);
  }

  protected ProcessStatus processChange(int index) throws RuntimeException {
    Object obj = this.methods.get(index).get();
    if (validators.get(index) != null && !validators.get(index).test(obj)) {
      return ProcessStatus.FAILURE;
    }
    setters.get(index).accept(rawObject, obj);
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
