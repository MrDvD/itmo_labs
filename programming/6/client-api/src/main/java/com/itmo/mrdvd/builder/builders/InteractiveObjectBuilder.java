package com.itmo.mrdvd.builder.builders;

import com.itmo.mrdvd.builder.ProcessStatus;
import com.itmo.mrdvd.builder.interactors.Interactor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveObjectBuilder<T> extends ObjectBuilder<T> implements InteractiveBuilder<T> {
  protected final List<Interactor<?>> interactors;
  protected final List<InteractiveBuilder<?>> builders;

  public InteractiveObjectBuilder() {
    this(
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveObjectBuilder(
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveBuilder<?>> builders) {
    super(setters, methods, validators);
    this.interactors = interactors;
    this.builders = builders;
  }

  public <U> void addInteractiveSetter(BiConsumer<T, U> setter, Interactor<?> inter)
      throws IllegalArgumentException {
    addInteractiveSetter(setter, inter, null);
  }

  @Override
  public <U> void addInteractiveSetter(
      BiConsumer<T, U> setter, Interactor<?> inter, Predicate<U> validator)
      throws IllegalArgumentException {
    if (inter == null) {
      throw new IllegalArgumentException("Не предоставлен интерактор для сеттера.");
    }
    set(setter, null, validator);
    interactors.set(interactors.size() - 1, inter);
  }

  @Override
  public <U> void addInteractiveBuilder(BiConsumer<T, U> setter, InteractiveBuilder<U> builder)
      throws IllegalArgumentException {
    set(setter, null);
    builders.set(builders.size() - 1, builder);
  }

  @Override
  public <U> void set(BiConsumer<T, U> setter, Supplier<U> method) throws IllegalArgumentException {
    this.set(setter, method, null);
  }

  @Override
  public <U> void set(BiConsumer<T, U> setter, Supplier<U> method, Predicate<U> validator)
      throws IllegalArgumentException {
    super.set(setter, method, validator);
    interactors.add(null);
    builders.add(null);
  }

  @Override
  protected ProcessStatus processSetter(int index) throws RuntimeException {
    Optional<?> result;
    Interactor<?> inter = null;
    if (builders.get(index) != null) {
      result = builders.get(index).build();
      if (result.isEmpty()) {
        throw new RuntimeException("Не удалось построить объект.");
      }
    } else {
      inter = interactors.get(index);
      if (inter == null) {
        return super.processSetter(index);
      }
      try {
        result = inter.ask();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (result.isPresent()
        && (validators.get(index) == null || validators.get(index).test(result.get()))) {
      setters.get(index).accept(rawObject, result.get());
    } else {
      if (inter != null) {
        try {
          inter.showError();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      return ProcessStatus.FAILURE;
    }
    return ProcessStatus.SUCCESS;
  }

  @Override
  protected Optional<T> getObject() {
    for (int i = 0; i < setters.size(); i++) {
      try {
        ProcessStatus status = processSetter(i);
        while (status.equals(ProcessStatus.FAILURE)) {
          status = processSetter(i);
        }
      } catch (RuntimeException e) {
        return Optional.empty();
      }
    }
    return Optional.of(rawObject);
  }
}
