package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.function.Function;

import com.itmo.mrdvd.object.Coordinates;

public class CoordinatesBuilder extends Builder<Coordinates> {
   public static class CoordinatesValidator {
      public static boolean validateX(Float x) {
        return x != null;
      }
  
      public static boolean validateY(Float y) {
        return y != null;
      }
    }
    
   public CoordinatesBuilder(Coordinates raw) {
    super(raw);
   }
   public CoordinatesBuilder(Coordinates raw, List<Function<Object, Void>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
    super(raw, setters, objects, validators);
   }
}