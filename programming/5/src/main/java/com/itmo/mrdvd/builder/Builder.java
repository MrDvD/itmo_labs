package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class Builder<T> {
   private final List<Function<Object, Void>> setters;
   private final List<Object> objects;
   private final List<Function<Object, Boolean>> validators;
   private final T object;

   public Builder(T rawObject, List<Function<Object, Void>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
      this.object = rawObject;
      this.setters = setters;
      this.objects = objects;
      this.validators = validators;
   }

   public Builder<T> attr(Function<Object, Void> setter, Object value) {
      return attr(setter, value, null);
   }

   public Builder<T> attr(Function<Object, Void> setter, Object value, Function<Object, Boolean> validator) {
      if (setter == null) {
         throw new IllegalArgumentException("Setter не может быть null.");
      }
      setters.add(setter);
      objects.add(value);
      validators.add(validator);
      return this;
   }

   public Optional<T> build() {
      for (int i = 0; i < setters.size(); i++) {
         if (validators.get(i) != null && !validators.get(i).apply(objects.get(i))) {
            return Optional.empty();
         }
         setters.get(i).apply(objects.get(i));
      }
      return Optional.of(object);
   }
}
