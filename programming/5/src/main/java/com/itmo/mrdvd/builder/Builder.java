package com.itmo.mrdvd.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Builder<T> {
   protected final List<TypedBiConsumer<T,?>> setters;
   protected final List<Object> objects;
   protected final List<Function<Object, Boolean>> validators;
   protected final T rawObject;

   public Builder(T rawObject) {
      this(rawObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public Builder(T rawObject, List<TypedBiConsumer<T,?>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
      this.rawObject = rawObject;
      this.setters = setters;
      this.objects = objects;
      this.validators = validators;
   }

   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls) {
      return attr(setter, value, valueCls, null);
   }

   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls, Function<Object, Boolean> validator) {
      if (setter == null) {
         throw new IllegalArgumentException("Setter не может быть null.");
      }
      setters.add(TypedBiConsumer.of(valueCls, setter));
      objects.add(value);
      validators.add(validator);
      return this;
   }

   public Optional<T> build() {
      for (int i = 0; i < setters.size(); i++) {
         if (validators.get(i) != null && !validators.get(i).apply(objects.get(i))) {
            return Optional.empty();
         }
         setters.get(i).acceptRaw(rawObject, objects.get(i));
      }
      return Optional.of(rawObject);
   }
}
