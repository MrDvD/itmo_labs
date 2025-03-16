package com.itmo.mrdvd.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.device.OutputDevice;

public abstract class InteractiveBuilder<T> extends Builder<T> {
   private final List<UserInteractor<?>> interactors;
   private final List<InteractiveBuilder<?>> builders;
   private final List<IndexedFunction<ProcessStatus>> methods;
   private final OutputDevice out;

   protected class UserInteractor<U> {
      private final String attributeName;
      private final Supplier<Optional<U>> inMethod;
      private final Optional<List<String>> options;
      private final String error;
      private final Optional<String> comment;

      protected UserInteractor(String attributeName, Supplier<Optional<U>> inMethod, String error) {
         this(attributeName, inMethod, error, null, null);
      }

      protected UserInteractor(String attributeName, Supplier<Optional<U>> inMethod, String error, String comment) {
         this(attributeName, inMethod, error, null, comment);
      }

      protected UserInteractor(String attributeName, Supplier<Optional<U>> inMethod, String error, List<String> options) {
         this(attributeName, inMethod, error, options, null);
      }
   
      protected UserInteractor(String attributeName, Supplier<Optional<U>> inMethod, String error, List<String> options, String comment) {
         this.attributeName = attributeName;
         this.inMethod = inMethod;
         this.error = error;
         this.options = Optional.ofNullable(options);
         this.comment = Optional.ofNullable(comment);
      }
   
      protected String attributeName() {
         return this.attributeName;
      }

      protected Supplier<Optional<U>> inMethod() {
         return this.inMethod;
      }
   
      protected Optional<String> comment() {
         return this.comment;
      }

      protected String error() {
         return this.error;
      }
   }

   public InteractiveBuilder(T rawObject, OutputDevice out) {
      this(rawObject, out, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
   }

   public InteractiveBuilder(T rawObject, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<T,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<IndexedFunction<ProcessStatus>> methods) {
      super(rawObject, setters, objects, validators);
      this.out = out;
      this.interactors = interactors;
      this.builders = builders;
      this.methods = methods;
   }

   public <U> InteractiveBuilder<T> addInteractiveBuilder(InteractiveBuilder<U> builder) {
      methods.add(IndexedFunction.of(this::processBuilder, builders.size()));
      builders.add(builder);
      return this;
   }

   public <U> InteractiveBuilder<T> addInteractiveSetter(BiConsumer<T, U> setter, Class<U> valueCls, UserInteractor<?> inter) throws IllegalArgumentException {
      return addInteractiveSetter(setter, valueCls, inter, null);
   }

   public <U> InteractiveBuilder<T> addInteractiveSetter(BiConsumer<T, U> setter, Class<U> valueCls, UserInteractor<?> inter, TypedPredicate<U> validator) throws IllegalArgumentException {
      if (inter == null) {
         throw new IllegalArgumentException("Метаданные не могут быть null.");
      }
      attr(setter, null, valueCls, validator);
      methods.add(IndexedFunction.of(this::processSetter, interactors.size() - 1));
      interactors.set(interactors.size() - 1, inter);
      return this;
   }

   @Override
   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls) {
      super.attr(setter, value, valueCls);
      interactors.add(null);
      return this;
   }

   @Override
   public <U> Builder<T> attr(BiConsumer<T,U> setter, Object value, Class<U> valueCls, Predicate<U> validator) {
      super.attr(setter, value, valueCls, validator);
      interactors.add(null);
      return this;
   }

   protected ProcessStatus processBuilder(int index) {
      return builders.get(index).interactiveBuild().isPresent() ? ProcessStatus.SUCCESS : ProcessStatus.FAILURE;
   }

   @Override
   protected ProcessStatus processSetter(int index) {
      UserInteractor<?> inter = interactors.get(index);
      if (inter == null) {
         return super.processSetter(index);
      }
      String msg = "";
      if (inter.options.isPresent()) {
         msg += String.format("Выберите поле \"%s\" из списка:\n", inter.attributeName());
         for (int j = 0; j < inter.options.get().size(); j++) {
            msg += String.format("* %s\n", inter.options.get().get(j));
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
      Optional<?> result = inter.inMethod().get();
      if (result.isPresent() && (validators.get(index) == null || validators.get(index).testRaw(result.get()))) {
         setters.get(index).acceptRaw(rawObject, result.get());
      } else {
         out.writeln(inter.error());
         return ProcessStatus.FAILURE;
      }
      return ProcessStatus.SUCCESS;
   }
   
   public Optional<T> interactiveBuild() {
      for (int i = 0; i < methods.size(); i++) {
         while (methods.get(i).apply(methods.get(i).index()).equals(ProcessStatus.FAILURE)) {}
      }
      return Optional.of(rawObject);
   }
}
