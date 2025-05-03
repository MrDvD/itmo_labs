package com.itmo.mrdvd.builder.builders;

import com.itmo.mrdvd.builder.ProcessStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ObjectBuilder<T> implements Builder<T> {
  protected final List<BiConsumer> setters;
  protected final List<Supplier<?>> methods;
  protected final List<Predicate> validators;
  protected Supplier<T> newMethod;
  protected T rawObject;

  public ObjectBuilder() {
    this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public ObjectBuilder(
      List<BiConsumer> setters, List<Supplier<?>> methods, List<Predicate> validators) {
    this.setters = setters;
    this.methods = methods;
    this.validators = validators;
  }

  @Override
  public ObjectBuilder<T> of(Supplier<T> newMethod) {
    this.newMethod = newMethod;
    return this;
  }

  @Override
  public <U> ObjectBuilder<T> set(BiConsumer<T, U> setter, Supplier<U> method) {
    return set(setter, method, null);
  }

  @Override
  public <U> ObjectBuilder<T> set(
      BiConsumer<T, U> setter, Supplier<U> method, Predicate<U> validator) {
    if (setter == null) {
      throw new IllegalArgumentException("Setter не может быть null.");
    }
    setters.add(setter);
    methods.add(method);
    validators.add(validator);
    return this;
  }

  protected ProcessStatus processSetter(int index) {
    Object attr = this.methods.get(index).get();
    if (validators.get(index) != null && !validators.get(index).test(attr)) {
      return ProcessStatus.FAILURE;
    }
    setters.get(index).accept(rawObject, attr);
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
      throw new IllegalStateException("Не предоставлен конструктор объекта.");
    }
    this.rawObject = newMethod.get();
    return getObject();
  }
}
