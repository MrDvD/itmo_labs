package com.itmo.mrdvd.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;

public class ObjectBuilder<T> implements Updater<T> {
   protected final List<TypedBiConsumer<T,?>> setters;
   protected final List<Object> objects;
   protected final List<Supplier<?>> methods;
   protected final List<TypedPredicate<?>> validators;
   protected Supplier<T> newMethod;
   protected T rawObject;

   public ObjectBuilder() {
      this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public ObjectBuilder(List<TypedBiConsumer<T,?>> setters, List<Object> objects, List<Supplier<?>> methods, List<TypedPredicate<?>> validators) {
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

   public <U> ObjectBuilder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls) {
      return attr(setter, value, valueCls, null);
   }

   @Override
   public <U> ObjectBuilder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls, Predicate<U> validator) {
      if (setter == null) {
         throw new IllegalArgumentException("Setter не может быть null.");
      }
      setters.add(TypedBiConsumer.of(valueCls, setter));
      objects.add(value);
      methods.add(null);
      validators.add(validator != null ? TypedPredicate.of(valueCls, validator) : null);
      return this;
   }

   public <U> ObjectBuilder<T> attrFromMethod(BiConsumer<T,U> setter, Supplier<U> method, Class<U> valueCls) {
      return attr(setter, method, valueCls, null);
   }

   @Override
   public <U> ObjectBuilder<T> attrFromMethod(BiConsumer<T,U> setter, Supplier<U> method, Class<U> valueCls, Predicate<U> validator) {
      if (setter == null) {
         throw new IllegalArgumentException("Setter не может быть null.");
      }
      setters.add(TypedBiConsumer.of(valueCls, setter));
      objects.add(null);
      methods.add(method);
      validators.add(validator != null ? TypedPredicate.of(valueCls, validator) : null);
      return this;
   }

   protected ProcessStatus processSetter(int index) {
      if (methods.get(index) != null) {
         objects.set(index, methods.get(index).get());
      }
      if (validators.get(index) != null && !validators.get(index).testRaw(objects.get(index))) {
         return ProcessStatus.FAILURE;
      }
      setters.get(index).acceptRaw(rawObject, objects.get(index));
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
      return update(newMethod.get());
   }

   @Override
   public Optional<T> update(T rawObject) throws IllegalArgumentException {
      if (rawObject == null) {
         throw new IllegalArgumentException("Объект не может быть null.");
      }
      this.rawObject = rawObject;
      return getObject();
   }
}
