package com.itmo.mrdvd.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.itmo.mrdvd.device.input.InteractiveInputDevice;

public abstract class InteractiveBuilder<T> extends Builder<T> {
   private final List<UserInteractor<?>> interactors;
   private final InteractiveInputDevice in;

   protected abstract class UserInteractor<U> {
      private final String attributeName;
      private final BiFunction<InteractiveInputDevice,String,Optional<U>> inMethod;
      private final Optional<String> comment;
   
      public UserInteractor(String attributeName, BiFunction<InteractiveInputDevice,String,Optional<U>> inMethod) {
         this(attributeName, inMethod, null);
      }
   
      public UserInteractor(String attributeName, BiFunction<InteractiveInputDevice,String,Optional<U>> inMethod, String comment) {
         this.attributeName = attributeName;
         this.inMethod = inMethod;
         this.comment = Optional.ofNullable(comment);
      }
   
      public String attributeName() {
         return this.attributeName;
      }

      public BiFunction<InteractiveInputDevice,String,Optional<U>> inMethod() {
         return this.inMethod;
      }
   
      public Optional<String> comment() {
         return this.comment;
      }
   }

   public InteractiveBuilder(T rawObject, InteractiveInputDevice in) {
      this(rawObject, in, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public InteractiveBuilder(T rawObject, InteractiveInputDevice in, List<UserInteractor<?>> interactors, List<TypedBiConsumer<T,?>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
      super(rawObject, setters, objects, validators);
      this.in = in;
      this.interactors = interactors;
   }

   public <U> Builder<T> addAttr(BiConsumer<T, U> setter, Class<U> valueCls, UserInteractor<?> inter) throws IllegalArgumentException {
      return addAttr(setter, valueCls, inter, null);
   }

   public <U> Builder<T> addAttr(BiConsumer<T, U> setter, Class<U> valueCls, UserInteractor<?> inter, Function<Object, Boolean> validator) throws IllegalArgumentException {
      if (inter == null) {
         throw new IllegalArgumentException("Метаданные не могут быть null.");
      }
      attr(setter, null, valueCls, validator);
      interactors.add(inter);
      return this;
   }

   public Optional<T> interactiveBuild() {
      for (int i = 0; i < setters.size(); i++) {
         UserInteractor<?> inter = interactors.get(i);
         String msg = String.format("Введите поле \"%s\"", inter.attributeName());
         if (inter.comment().isPresent()) {
            msg += String.format(" (%s)", inter.comment().get());
         }
         msg += ": ";
         Optional<?> result = inter.inMethod().apply(in, msg);
         if (result.isPresent() && (validators.get(i) == null || !validators.get(i).apply(result.get()))) {
            setters.get(i).acceptRaw(rawObject, result.get());
         } else {
            return Optional.empty();
         }
      }
      return Optional.of(rawObject);
   }
}
