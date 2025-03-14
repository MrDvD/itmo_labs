package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;

public abstract class InteractiveBuilder<T> {
   private final InteractiveInputDevice in;
   private final OutputDevice out;
   private final List<SetterWrapper<?>> setters;
   private final T object;

   public InteractiveBuilder(T rawObject, List<SetterWrapper<?>> setters, InteractiveInputDevice in, OutputDevice out) {
      this.object = rawObject;
      this.setters = setters;
      this.in = in;
      this.out = out;
   }

   public void addSetter(SetterWrapper<?> setter) {
      setters.add(setter);
   }

   public T build() {
      for (SetterWrapper<?> f : setters) {
         String msg = String.format("Введите поле \"%s\"", f.name());
         if (f.comment().isPresent()) {
            msg += String.format(" (%s)", f.comment().get());
         }
         msg += ": ";
         Optional<String> result = in.read(msg);
         if (result.isPresent() && f.validate(result.get())) {
            f.setter()(result.get());
         }
      }
   }
}
