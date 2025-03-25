package com.itmo.mrdvd.builder.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.ProcessStatus;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;

public class InteractiveObjectBuilder<T> extends ObjectBuilder<T> implements InteractiveBuilder<T> {
  private final List<Interactor<?>> interactors;
  private final List<InteractiveBuilder<?>> builders;
  private final OutputDevice out;

  public InteractiveObjectBuilder(OutputDevice out) {
    this(
        out,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveObjectBuilder(
      OutputDevice out,
      List<Interactor<?>> interactors,
      List<TypedBiConsumer<T, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<InteractiveBuilder<?>> builders) {
    super(setters, objects, methods, validators);
    this.out = out;
    this.interactors = interactors;
    this.builders = builders;
  }

  public <U> InteractiveObjectBuilder<T> addInteractiveSetter(
      BiConsumer<T, U> setter, Class<U> valueCls, Interactor<?> inter)
      throws IllegalArgumentException {
    return addInteractiveSetter(setter, valueCls, inter, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T> addInteractiveSetter(
      BiConsumer<T, U> setter, Class<U> valueCls, Interactor<?> inter, TypedPredicate<U> validator)
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

  public <U> InteractiveObjectBuilder<T> addInteractiveBuilder(
      InteractiveBuilder<U> builder, BiConsumer<T, U> setter, Class<U> valueCls)
      throws IllegalArgumentException {
    return addInteractiveBuilder(builder, setter, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T> addInteractiveBuilder(
      InteractiveBuilder<U> builder,
      BiConsumer<T, U> setter,
      Class<U> valueCls,
      TypedPredicate<U> validator)
      throws IllegalArgumentException {
    set(setter, null, valueCls, validator);
    builders.set(builders.size() - 1, builder);
    return this;
  }

  @Override
  public <U> InteractiveObjectBuilder<T> setFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls)
      throws IllegalArgumentException {
    return this.setFromMethod(setter, method, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T> setFromMethod(
      BiConsumer<T, U> setter, Supplier<U> method, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException {
    super.setFromMethod(setter, method, valueCls, validator);
    interactors.add(null);
    builders.add(null);
    return this;
  }

  @Override
  public <U> InteractiveObjectBuilder<T> set(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls) throws IllegalArgumentException {
    return this.set(setter, value, valueCls, null);
  }

  @Override
  public <U> InteractiveObjectBuilder<T> set(
      BiConsumer<T, U> setter, Object value, Class<U> valueCls, Predicate<U> validator)
      throws IllegalArgumentException {
    super.set(setter, value, valueCls, validator);
    interactors.add(null);
    builders.add(null);
    return this;
  }

  @Override
  protected ProcessStatus processSetter(int index) {
    Optional<?> result;
    Interactor<?> inter = null;
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
      result = inter.get();
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
      while (processSetter(i).equals(ProcessStatus.FAILURE)) {}
    }
    return Optional.of(rawObject);
  }
}
