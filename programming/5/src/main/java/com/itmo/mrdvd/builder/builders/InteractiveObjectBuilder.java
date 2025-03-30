package com.itmo.mrdvd.builder.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.ProcessStatus;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InputDevice;

public class InteractiveObjectBuilder<T, K extends InputDevice> extends ObjectBuilder<T>
    implements InteractiveBuilder<T, K> {
  protected final List<Interactor<?, K>> interactors;
  protected final List<InteractiveBuilder<?, ?>> builders;
  protected final K in;
  protected final OutputDevice out;

  public InteractiveObjectBuilder(K in, OutputDevice out) {
    this(
        in,
        out,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveObjectBuilder(
      K in,
      OutputDevice out,
      List<Interactor<?, K>> interactors,
      List<BiConsumer> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveBuilder<?, ?>> builders) {
    super(setters, objects, methods, validators);
    this.in = in;
    this.out = out;
    this.interactors = interactors;
    this.builders = builders;
  }

  public <U> InteractiveObjectBuilder<T, K> addInteractiveSetter(
      BiConsumer<T, U> setter, Class<U> valueCls, Interactor<?, K> inter)
      throws IllegalArgumentException {
    return addInteractiveSetter(setter, valueCls, inter, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T, K> addInteractiveSetter(
      BiConsumer<T, U> setter,
      Class<U> valueCls,
      Interactor<?, K> inter,
      Predicate<U> validator)
      throws IllegalArgumentException {
    if (inter == null) {
      throw new IllegalArgumentException("Метаданные не могут быть null.");
    }
    if (setter == null) {
      throw new IllegalArgumentException("Setter не может быть null.");
    }
    set(setter, null, valueCls, validator);
    interactors.set(interactors.size() - 1, inter);
    return this;
  }

  public <U> InteractiveObjectBuilder<T, K> addInteractiveBuilder(
      InteractiveBuilder<U, ?> builder, BiConsumer<T, U> setter, Class<U> valueCls)
      throws IllegalArgumentException {
    return addInteractiveBuilder(builder, setter, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T, K> addInteractiveBuilder(
      InteractiveBuilder<U, ?> builder,
      BiConsumer<T, U> setter,
      Class<U> valueCls,
      Predicate<U> validator)
      throws IllegalArgumentException {
    set(setter, null, valueCls, validator);
    builders.set(builders.size() - 1, builder);
    return this;
  }

  @Override
  public <U> InteractiveObjectBuilder<T, K> setFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls)
      throws IllegalArgumentException {
    return this.setFromMethod(setter, method, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T, K> setFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException {
    super.setFromMethod(setter, method, valueCls, validator);
    interactors.add(null);
    builders.add(null);
    return this;
  }

  @Override
  public <U> InteractiveObjectBuilder<T, K> set(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls) throws IllegalArgumentException {
    return this.set(setter, value, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T, K> set(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException {
    super.set(setter, value, valueCls, validator);
    interactors.add(null);
    builders.add(null);
    return this;
  }

  @Override
  protected ProcessStatus processSetter(int index) throws RuntimeException {
    if (getIn().isEmpty()) {
      throw new NullPointerException("InputDevice не может быть null.");
    }
    Optional<?> result;
    Interactor<?, K> inter = null;
    if (builders.get(index) != null) {
      result = builders.get(index).build();
    } else {
      inter = interactors.get(index);
      if (inter == null) {
        return super.processSetter(index);
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
        && (validators.get(index) == null || validators.get(index).test(result.get()))) {
      setters.get(index).accept(rawObject, result.get());
    } else {
      out.writeln(inter != null ? inter.error() : "\n[ERROR]: Не удалось сформировать поле");
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

  @Override
  public InteractiveObjectBuilder<T, K> setIn(K in) {
    return (new InteractiveObjectBuilder<T, K>(in, out)).of(newMethod);
  }

  @Override
  public Optional<K> getIn() {
    return Optional.ofNullable(in);
  }

  @Override
  public InteractiveObjectBuilder<T, K> of(Supplier<T> newMethod) {
    this.newMethod = newMethod;
    return this;
  }
}
