package com.itmo.mrdvd.validators;

import com.itmo.mrdvd.Coordinates;

public class CoordinatesValidator extends ObjectValidator<Coordinates> {
  public static boolean validateX(Float x) {
    return x != null;
  }

  public static boolean validateY(Float y) {
    return y != null;
  }

  private void init() {
    check(Coordinates::getX, Float.class, CoordinatesValidator::validateX);
    check(Coordinates::getY, Float.class, CoordinatesValidator::validateY);
  }

  public CoordinatesValidator() {
    super();
    init();
  }
}
