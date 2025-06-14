package com.itmo.mrdvd.validators;

import com.itmo.mrdvd.Event;
import com.itmo.mrdvd.EventType;

public class EventValidator extends ObjectValidator<Event> {
  public static boolean validateId(Long id) {
    return id != null && id > 0;
  }

  public static boolean validateName(String name) {
    return name != null && !name.isBlank();
  }

  public static boolean validateDescription(String description) {
    return description != null && !description.isBlank() && description.length() <= 1190;
  }

  public static boolean validateType(EventType type) {
    return type != null;
  }

  private void init() {
    check(Event::getName, String.class, EventValidator::validateName);
    check(Event::getDesc, String.class, EventValidator::validateDescription);
    check(Event::getType, EventType.class, EventValidator::validateType);
  }

  public EventValidator() {
    super();
    init();
  }
}
