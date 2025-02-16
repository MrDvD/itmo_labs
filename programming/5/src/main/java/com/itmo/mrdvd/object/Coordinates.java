package com.itmo.mrdvd.object;

public class Coordinates implements Validatable {
   private Long x, y;
   public static class CoordinatesValidator {
      public static boolean validateX(String x) {
         return x != null;
      }
      public static boolean validateX(Long x) {
         return x != null;
      }
      public static boolean validateY(String y) {
         return y != null;
      }
      public static boolean validateY(Long y) {
         return y != null;
      }
   }
   @Override
   public boolean isValid() {
      return CoordinatesValidator.validateX(getX()) && CoordinatesValidator.validateY(getY());
   }
   public int setX(Long x) {
      if (CoordinatesValidator.validateX(x)) {
         this.x = x;
         return 0;
      }
      return -1;
   }
   public int setX(String x) {
      // parse
      return setX(x);
   }
   public int setY(Long y) {
      if (CoordinatesValidator.validateY(y)) {
         this.y = y;
         return 0;
      }
      return -1;
   }
   public int setY(String y) {
      // parse
      return setY(y);
   }
   public Long getX() {
      return x;
   }
   public Long getY() {
      return y;
   }
}
