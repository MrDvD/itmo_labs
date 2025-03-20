package com.itmo.mrdvd.builder.validators;

import java.time.LocalDateTime;

import com.itmo.mrdvd.object.TicketType;

public class TicketValidator {
   public static boolean validateCreationDate(LocalDateTime creationDate) {
    return creationDate != null;
  }

  public static boolean validateName(String name) {
    return name != null && !name.isBlank();
  }

  public static boolean validatePrice(Integer cost) {
    return cost > 0;
  }

  public static boolean validateType(TicketType type) {
    return type != null;
  }
}