package com.itmo.mrdvd.builder.examples;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.InteractiveBuilder;
import com.itmo.mrdvd.builder.InteractiveObjectBuilder;
import com.itmo.mrdvd.builder.InteractiveUpdater;
import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.object.Coordinates;

public class CoordinatesBuilder extends InteractiveObjectBuilder<Coordinates> {
   public static class CoordinatesValidator {
      public static boolean validateX(Float x) {
        return x != null;
      }
  
      public static boolean validateY(Float y) {
        return y != null;
      }
    }

    private void init(FloatInputDevice in) {
      of(Coordinates::new);
      addInteractiveSetter(Coordinates::setX, Float.class, new UserInteractor<Float>("X-координата", () -> { Optional<Float> res = in.readFloat(); in.skipLine(); return res; } , "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).", "разделитель - точка"), CoordinatesValidator::validateX);
      addInteractiveSetter(Coordinates::setY, Float.class, new UserInteractor<Float>("Y-координата", () -> { Optional<Float> res = in.readFloat(); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).", "разделитель - точка"), CoordinatesValidator::validateY);
    }

   public CoordinatesBuilder(FloatInputDevice in, OutputDevice out) {
    super(out);
    init(in);
   }
   public CoordinatesBuilder(FloatInputDevice in, OutputDevice out, List<Interactor<?>> interactors, List<TypedBiConsumer<Coordinates,?>> setters, List<Object> objects, List<Supplier<?>> methods, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<Function<Coordinates,?>> getters, List<InteractiveUpdater<?>> updaters) {
    super(out, interactors, setters, objects, methods, validators, builders, getters, updaters);
    init(in);
   }
}