package com.itmo.mrdvd.builder.builders;

import com.itmo.mrdvd.builder.ProcessStatus;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SuppliedObjectBuilder<T> extends ObjectBuilder<T> implements SuppliedBuilder<T> {
  protected final List<SuppliedBuilder<?>> builders;
  protected final Set<SuppliedBuilder<?>> hashedBuilders;
  protected List<?> elements;
  protected int suppliedIndex;

  public SuppliedObjectBuilder() {
    this(
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new HashSet<>());
  }

  public SuppliedObjectBuilder(
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<SuppliedBuilder<?>> builders,
      Set<SuppliedBuilder<?>> hashedBuilders) {
    super(setters, methods, validators);
    this.builders = builders;
    this.hashedBuilders = hashedBuilders;
  }

  @Override
  public SuppliedObjectBuilder<T> withElements(List<?> elems) {
    this.elements = elems;
    for (SuppliedBuilder<?> b : this.hashedBuilders) {
      b.withElements(elems);
    }
    return this;
  }

  @Override
  public <U> SuppliedBuilder<T> addSuppliedBuilder(
      BiConsumer<T, U> setter, SuppliedBuilder<U> builder) throws IllegalArgumentException {
    setFromSupplies(setter);
    this.builders.set(this.builders.size() - 1, builder);
    return this;
  }

  @Override
  public <U> SuppliedBuilder<T> setFromSupplies(BiConsumer<T, U> setter)
      throws IllegalArgumentException {
    return set(setter, null);
  }

  @Override
  public <U> SuppliedBuilder<T> setFromSupplies(BiConsumer<T, U> setter, Predicate<U> validator)
      throws IllegalArgumentException {
    return set(setter, null, validator);
  }

  @Override
  public <U> SuppliedObjectBuilder<T> set(BiConsumer<T, U> setter, Supplier<U> method) {
    return set(setter, method, null);
  }

  @Override
  public <U> SuppliedObjectBuilder<T> set(
      BiConsumer<T, U> setter, Supplier<U> method, Predicate<U> validator) {
    super.set(setter, method, validator);
    this.builders.add(null);
    return this;
  }

  @Override
  protected ProcessStatus processSetter(int index) throws IllegalStateException {
    Object attr;
    if (this.methods.get(index) == null) {
      if (this.elements == null) {
        throw new IllegalStateException("Не предоставлен набор элементов для построения объекта.");
      }
      if (this.elements.size() > this.suppliedIndex + 1) {
        this.suppliedIndex++;
        attr = this.elements.get(this.suppliedIndex);
      } else {
        throw new IllegalArgumentException("Недостаточно элементов для построения объекта.");
      }
    } else {
      attr = this.methods.get(index).get();
    }
    if (validators.get(index) != null && !validators.get(index).test(attr)) {
      return ProcessStatus.FAILURE;
    }
    setters.get(index).accept(rawObject, attr);
    return ProcessStatus.SUCCESS;
  }

  @Override
  public Optional<T> build() {
    this.suppliedIndex = 0;
    return super.build();
  }
}
