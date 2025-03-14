package com.itmo.mrdvd.builder;

import java.util.Optional;
import java.util.function.Function;

public abstract class SetterWrapper<T> {
   private final Function<T, Void> setter;
   private final String name;
   private final Optional<String> comment;

   public SetterWrapper(Function<T, Void> setter, String name) {
      this(setter, name, null);
   }

   public SetterWrapper(Function<T, Void> setter, String name, String comment) {
      this.setter = setter;
      this.name = name;
      this.comment = Optional.ofNullable(comment);
   }

   public Function<T, Void> setter() {
      return this.setter;
   }

   public String name() {
      return this.name;
   }

   public Optional<String> comment() {
      return this.comment;
   }
}
