package com.itmo.mrdvd.builder.updaters;

import com.itmo.mrdvd.builder.ProcessStatus;
import com.itmo.mrdvd.builder.interactors.Interactor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveObjectUpdater<T> extends ObjectUpdater<T> implements InteractiveUpdater<T> {
  protected final List<Interactor<?>> interactors;
  protected final List<Function<T, ?>> getters;
  protected final List<InteractiveUpdater> updaters;

  public InteractiveObjectUpdater() {
    this(
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveObjectUpdater(
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Function<T, ?>> getters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveUpdater> updaters) {
    super(setters, methods, validators);
    this.interactors = interactors;
    this.updaters = updaters;
    this.getters = getters;
  }

  @Override
  public <U> void addInteractiveChange(
      BiConsumer<T, U> setter, Function<T, U> getter, Interactor<?> inter, Predicate<U> validator)
      throws IllegalArgumentException {
    if (inter == null) {
      throw new IllegalArgumentException("Не предоставлен интерактор для сеттера.");
    }
    change(setter, null, validator);
    this.interactors.set(interactors.size() - 1, inter);
    this.getters.set(getters.size() - 1, getter);
  }

  @Override
  public <U> void addInteractiveUpdater(
      BiConsumer<T, U> setter, Function<T, U> getter, InteractiveUpdater<U> updater)
      throws IllegalArgumentException {
    change(setter, null);
    this.updaters.set(updaters.size() - 1, updater);
    this.getters.set(getters.size() - 1, getter);
  }

  @Override
  public <U> void change(BiConsumer<T, U> setter, Supplier<U> method)
      throws IllegalArgumentException {
    change(setter, method, null);
  }

  @Override
  public <U> void change(BiConsumer<T, U> setter, Supplier<U> method, Predicate<U> validator)
      throws IllegalArgumentException {
    super.change(setter, method, validator);
    this.interactors.add(null);
    this.updaters.add(null);
    this.getters.add(null);
  }

  @Override
  protected ProcessStatus processChange(int index) throws RuntimeException {
    Optional<?> result;
    Interactor<?> inter = null;
    if (updaters.get(index) != null) {
      result = updaters.get(index).update(getters.get(index).apply(rawObject));
      if (result.isEmpty()) {
        throw new RuntimeException("Не удалось построить объект.");
      }
    } else {
      inter = interactors.get(index);
      if (inter == null) {
        return super.processChange(index);
      }
      try {
        result = inter.ask(this.getters.get(index).toString());
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
        ProcessStatus status = processChange(i);
        while (status.equals(ProcessStatus.FAILURE)) {
          status = processChange(i);
        }
      } catch (RuntimeException e) {
        return Optional.empty();
      }
    }
    return Optional.of(rawObject);
  }
}
