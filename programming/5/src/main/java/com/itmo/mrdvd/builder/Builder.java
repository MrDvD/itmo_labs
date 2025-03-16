package com.itmo.mrdvd.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public abstract class Builder<T> {
   protected final List<TypedBiConsumer<T,?>> setters;
   protected final List<Object> objects;
   protected final List<TypedPredicate<?>> validators;
   protected final T rawObject;

   public Builder(T rawObject) {
      this(rawObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public Builder(T rawObject, List<TypedBiConsumer<T,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators) {
      this.rawObject = rawObject;
      this.setters = setters;
      this.objects = objects;
      this.validators = validators;
   }

   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls) {
      return attr(setter, value, valueCls, null);
   }

   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls, Predicate<U> validator) {
      if (setter == null) {
         throw new IllegalArgumentException("Setter не может быть null.");
      }
      setters.add(TypedBiConsumer.of(valueCls, setter));
      objects.add(value);
      validators.add(TypedPredicate.of(valueCls, validator));
      return this;
   }

   protected ProcessStatus processSetter(int index) {
      if (validators.get(index) != null && !validators.get(index).testRaw(objects.get(index))) {
         return ProcessStatus.FAILURE;
      }
      setters.get(index).acceptRaw(rawObject, objects.get(index));
      return ProcessStatus.SUCCESS;
   }

   public boolean isValid() {
      for (int i = 0; i < setters.size(); i++) {
         if (validators.get(i) != null && !validators.get(i).testRaw(objects.get(i))) {
            return false;
         }
      }
      return true;
   }

   public Optional<T> build() {
      for (int i = 0; i < setters.size(); i++) {
         if (processSetter(i).equals(ProcessStatus.FAILURE)) {
            return Optional.empty();
         }
      }
      return Optional.of(rawObject);
   }
}
