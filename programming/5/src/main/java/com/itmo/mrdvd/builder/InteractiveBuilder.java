package com.itmo.mrdvd.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.itmo.mrdvd.device.OutputDevice;

public abstract class InteractiveBuilder<T> extends Builder<T> {
   private final List<UserInteractor<?>> interactors;
   private final OutputDevice out;

   protected class UserInteractor<U> {
      private final String attributeName;
      private final Supplier<Optional<U>> inMethod;
      
      private final Optional<String> comment;
   
      public UserInteractor(String attributeName, Supplier<Optional<U>> inMethod) {
         this(attributeName, inMethod, null);
      }
   
      public UserInteractor(String attributeName, Supplier<Optional<U>> inMethod, String comment) {
         this.attributeName = attributeName;
         this.inMethod = inMethod;
         this.comment = Optional.ofNullable(comment);
      }
   
      public String attributeName() {
         return this.attributeName;
      }

      public Supplier<Optional<U>> inMethod() {
         return this.inMethod;
      }
   
      public Optional<String> comment() {
         return this.comment;
      }
   }

   public InteractiveBuilder(T rawObject, OutputDevice out) {
      this(rawObject, out, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public InteractiveBuilder(T rawObject, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<T,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators) {
      super(rawObject, setters, objects, validators);
      this.out = out;
      this.interactors = interactors;
   }

   public <U> Builder<T> addInteractiveSetter(BiConsumer<T, U> setter, Class<U> valueCls, UserInteractor<?> inter) throws IllegalArgumentException {
      return addInteractiveSetter(setter, valueCls, inter, null);
   }

   public <U> Builder<T> addInteractiveSetter(BiConsumer<T, U> setter, Class<U> valueCls, UserInteractor<?> inter, TypedPredicate<U> validator) throws IllegalArgumentException {
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
         out.write(msg);
         Optional<?> result = inter.inMethod().get();
         if (result.isPresent() && (validators.get(i) == null || !validators.get(i).testRaw(result.get()))) {
            setters.get(i).acceptRaw(rawObject, result.get());
         } else {
            return Optional.empty();
         }
      }
      return Optional.of(rawObject);
   }
}
