package com.itmo.mrdvd.object;

import org.apache.commons.lang3.math.NumberUtils;

public class Coordinates implements Validatable {
   private Float x, y;
   public static class CoordinatesValidator {
      public static boolean validateX(Float x) {
         return x != null;
      }
      public static boolean validateY(Float y) {
         return y != null;
      }
   }
   public static class CoordinatesParser {
      public static Float parseX(String x) {
         if (NumberUtils.isParsable(x)) {
            return NumberUtils.toFloat(x);
         }
         return null;
      }
      public static Float parseY(String y) {
         if (y != null && !y.isBlank() && NumberUtils.isParsable(y)) {
            return NumberUtils.toFloat(y);
         }
         return null;
      }
   }
   @Override
   public boolean isValid() {
      return CoordinatesValidator.validateX(getX()) &&
             CoordinatesValidator.validateY(getY());
   }
   public int setX(Float x) {
      return setX(x, true);
   }
   public int setX(Float x, boolean validate) {
      if (!validate || CoordinatesValidator.validateX(x)) {
         this.x = x;
         return 0;
      }
      return -1;
   }
   public int setY(Float y) {
      return setY(y, true);
   }
   public int setY(Float y, boolean validate) {
      if (!validate || CoordinatesValidator.validateY(y)) {
         this.y = y;
         return 0;
      }
      return -1;
   }
   public Float getX() {
      return x;
   }
   public Float getY() {
      return y;
   }
   @Override
   public String toString() {
      return String.format("(%.2f, %.2f)", getX(), getY());
   }
}
