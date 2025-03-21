package com.itmo.mrdvd.builder.validators;

import java.time.LocalDateTime;

import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class TicketValidator extends ObjectValidator<Ticket> {
   public static boolean validateId(Long id) {
      return id != null && id > 0;
   }

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

  private void init() {
   check(Ticket::getId, Long.class, TicketValidator::validateId);
   check(Ticket::getName, String.class, TicketValidator::validateName);
   check(Ticket::getCoordinates, new CoordinatesValidator());
   check(Ticket::getPrice, Integer.class, TicketValidator::validatePrice);
   check(Ticket::getType, TicketType.class, TicketValidator::validateType);
   check(Ticket::getEvent, new EventValidator());
   check(Ticket::getCreationDate, LocalDateTime.class, TicketValidator::validateCreationDate);
  }

  public TicketValidator() {
   super();
   init();
  }
}