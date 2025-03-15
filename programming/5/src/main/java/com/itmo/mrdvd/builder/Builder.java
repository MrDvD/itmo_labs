package com.itmo.mrdvd.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Builder<T> {
   private final List<TypedBiConsumer<T,?>> setters;
   private final List<Object> objects;
   private final List<Function<Object, Boolean>> validators;
   private final T object;

   public Builder(T rawObject) {
      this(rawObject, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public Builder(T rawObject, List<TypedBiConsumer<T,?>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
      this.object = rawObject;
      this.setters = setters;
      this.objects = objects;
      this.validators = validators;
   }

   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> cls) {
      return attr(setter, value, cls, null);
   }

   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> cls, Function<Object, Boolean> validator) {
      if (setter == null) {
         throw new IllegalArgumentException("Setter не может быть null.");
      }
      setters.add(TypedBiConsumer.of(cls, setter));
      objects.add(value);
      validators.add(validator);
      return this;
   }

   public Optional<T> build() {
      for (int i = 0; i < setters.size(); i++) {
         if (validators.get(i) != null && !validators.get(i).apply(objects.get(i))) {
            return Optional.empty();
         }
         setters.get(i).acceptRaw(object, objects.get(i));
      }
      return Optional.of(object);
   }
}
