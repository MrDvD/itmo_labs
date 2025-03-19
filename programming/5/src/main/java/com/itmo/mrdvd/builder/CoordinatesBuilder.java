package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.object.Coordinates;

public class CoordinatesBuilder extends InteractiveBuilder<Coordinates> {
   public static class CoordinatesValidator {
      public static boolean validateX(Float x) {
        return x != null;
      }
  
      public static boolean validateY(Float y) {
        return y != null;
      }
    }

    private void initSetters(FloatInputDevice in) {
      addInteractiveSetter(Coordinates::setX, Float.class, new UserInteractor<Float>("X-координата", () -> { Optional<Float> res = in.readFloat(); in.skipLine(); return res; } , "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).", "разделитель - точка"), CoordinatesValidator::validateX);
      addInteractiveSetter(Coordinates::setY, Float.class, new UserInteractor<Float>("Y-координата", () -> { Optional<Float> res = in.readFloat(); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).", "разделитель - точка"), CoordinatesValidator::validateY);
    }

   public CoordinatesBuilder(FloatInputDevice in, OutputDevice out) {
    super(new Coordinates(), out);
    initSetters(in);
   }
   public CoordinatesBuilder(FloatInputDevice in, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<Coordinates,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders) {
    super(new Coordinates(), out, interactors, setters, objects, validators, builders);
    initSetters(in);
   }
}