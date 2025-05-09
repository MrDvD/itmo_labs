package com.itmo.mrdvd.validators;

import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;
import java.time.LocalDateTime;

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

  private void init(Validator<Coordinates> coordinatesValidator, Validator<Event> eventValidator) {
    check(Ticket::getName, String.class, TicketValidator::validateName);
    check(Ticket::getCoordinates, coordinatesValidator);
    check(Ticket::getPrice, Integer.class, TicketValidator::validatePrice);
    check(Ticket::getType, TicketType.class, TicketValidator::validateType);
    check(Ticket::getEvent, eventValidator);
    check(Ticket::getCreationDate, LocalDateTime.class, TicketValidator::validateCreationDate);
  }

  public TicketValidator(
      Validator<Coordinates> coordinatesValidator, Validator<Event> eventValidator) {
    super();
    init(coordinatesValidator, eventValidator);
  }
}
