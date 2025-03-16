package com.itmo.mrdvd.builder;

import java.util.List;

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

    private void initSetters(FloatInputDevice inFloat) {
      addInteractiveSetter(Coordinates::setX, Float.class, new UserInteractor<Float>("X-координата", inFloat::readFloat, "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).", "разделитель - точка"), CoordinatesValidator::validateX);
      addInteractiveSetter(Coordinates::setY, Float.class, new UserInteractor<Float>("Y-координата", inFloat::readFloat, "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).", "разделитель - точка"), CoordinatesValidator::validateY);
    }

   public CoordinatesBuilder(FloatInputDevice inFloat, OutputDevice out) {
    super(new Coordinates(), out);
    initSetters(inFloat);
   }
   public CoordinatesBuilder(FloatInputDevice inFloat, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<Coordinates,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<IndexedFunction<ProcessStatus>> methods) {
    super(new Coordinates(), out, interactors, setters, objects, validators, builders, methods);
    initSetters(inFloat);
   }
}