package com.itmo.mrdvd.builder.updaters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.ProcessStatus;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InputDevice;

public class InteractiveObjectUpdater<T, K extends InputDevice> extends ObjectUpdater<T>
    implements InteractiveUpdater<T, K> {
  protected final List<Interactor<?, K>> interactors;
  protected final List<InteractiveUpdater> updaters;
  protected final List<Function<T, ?>> getters;
  protected final K in;
  protected final OutputDevice out;

  public InteractiveObjectUpdater(K in, OutputDevice out) {
    this(
        in,
        out,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveObjectUpdater(
      K in,
      OutputDevice out,
      List<Interactor<?, K>> interactors,
      List<TypedBiConsumer<T, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<Function<T, ?>> getters,
      List<InteractiveUpdater> updaters) {
    super(setters, objects, methods, validators);
    this.in = in;
    this.out = out;
    this.interactors = interactors;
    this.updaters = updaters;
    this.getters = getters;
  }

  public <U> InteractiveObjectUpdater<T, K> addInteractiveChange(
      BiConsumer<T, U> setter, Function<T, U> getter, Class<U> valueCls, Interactor<?, K> inter)
      throws IllegalArgumentException {
    addInteractiveChange(setter, getter, valueCls, inter, null);
    getters.set(getters.size() - 1, getter);
    return this;
  }

  @Override
  public <U> InteractiveObjectUpdater<T, K> addInteractiveChange(
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      Interactor<?, K> inter,
      TypedPredicate<U> validator)
      throws IllegalArgumentException {
    if (inter == null) {
      throw new IllegalArgumentException("Метаданные не могут быть null.");
    }
    if (setter == null) {
      throw new IllegalArgumentException("Setter не может быть null.");
    }
    change(setter, null, valueCls, validator);
    interactors.set(interactors.size() - 1, inter);
    getters.set(getters.size() - 1, getter);
    return this;
  }

  public <U> InteractiveUpdater<T, K> addInteractiveUpdater(
      InteractiveUpdater<U, ?> updater,
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls)
      throws IllegalArgumentException {
    return addInteractiveUpdater(updater, setter, getter, valueCls, null);
  }

  @Override
  public <U> InteractiveUpdater<T, K> addInteractiveUpdater(
      InteractiveUpdater<U, ?> updater,
      BiConsumer<T, U> setter,
      Function<T, U> getter,
      Class<U> valueCls,
      TypedPredicate<U> validator)
      throws IllegalArgumentException {
    change(setter, null, valueCls, validator);
    getters.set(getters.size() - 1, getter);
    updaters.set(updaters.size() - 1, updater);
    return this;
  }

  @Override
  public <U> InteractiveObjectUpdater<T, K> changeFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls)
      throws IllegalArgumentException {
    return this.changeFromMethod(setter, method, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectUpdater<T, K> changeFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException {
    super.changeFromMethod(setter, method, valueCls, validator);
    interactors.add(null);
    getters.add(null);
    updaters.add(null);
    return this;
  }

  @Override
  public <U> InteractiveObjectUpdater<T, K> change(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls) throws IllegalArgumentException {
    return this.change(setter, value, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectUpdater<T, K> change(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException {
    super.change(setter, value, valueCls, validator);
    interactors.add(null);
    getters.add(null);
    updaters.add(null);
    return this;
  }

  @Override
  protected ProcessStatus processChange(int index) throws NullPointerException, RuntimeException {
    if (getIn().isEmpty()) {
      throw new NullPointerException("InputDevice не может быть null.");
    }
    Optional<?> result;
    Interactor<?, K> inter = null;
    if (updaters.get(index) != null) {
      result = updaters.get(index).update(getters.get(index).apply(rawObject));
    } else {
      inter = interactors.get(index);
      if (inter == null) {
        return super.processChange(index);
      }
      String msg = "";
      if (inter.options().isPresent()) {
        msg += String.format("Выберите поле \"%s\" из списка:\n", inter.attributeName());
        for (int j = 0; j < inter.options().get().size(); j++) {
          msg += String.format("* %s\n", inter.options().get().get(j));
        }
        msg += "Ваш выбор";
      } else {
        msg += String.format("Введите поле \"%s\"", inter.attributeName());
      }
      if (inter.comment().isPresent()) {
        msg += String.format(" (%s)", inter.comment().get());
      }
      if (getters.get(index) != null) {
        msg += String.format(" [%s]", getters.get(index).apply(rawObject));
      }
      msg += ": ";
      out.write(msg);
      try {
        result = inter.get(getIn().get());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (methods.get(index) != null) {
      objects.set(index, methods.get(index).get());
    }
    if (result.isPresent()
        && (validators.get(index) == null || validators.get(index).testRaw(result.get()))) {
      setters.get(index).acceptRaw(rawObject, result.get());
    } else {
      out.writeln(inter != null ? inter.error() : "[ERROR]: Не удалось сформировать поле");
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

  @Override
  public InteractiveObjectUpdater<T, K> setIn(K in) {
    return new InteractiveObjectUpdater<>(in, out);
  }

  @Override
  public Optional<K> getIn() {
    return Optional.ofNullable(in);
  }
}
