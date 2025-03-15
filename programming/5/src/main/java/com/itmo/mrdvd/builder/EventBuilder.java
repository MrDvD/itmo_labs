package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.function.Function;

import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;

public class EventBuilder extends Builder<Event> {
   public static class EventValidator {
    public static boolean validateId(Long id) {
      return id != null && id >= 0;
    }

    public static boolean validateName(String name) {
      return name != null && !name.isBlank();
    }

    public static boolean validateDescription(String description) {
      return description != null && !description.isBlank() && description.length() <= 1190;
    }

    public static boolean validateEventType(EventType type) {
      return type != null;
    }
  }

  public EventBuilder(Event raw) {
    super(raw);
  }

  public EventBuilder(Event raw, List<Function<Object, Void>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
    super(raw, setters, objects, validators);
  }
}
