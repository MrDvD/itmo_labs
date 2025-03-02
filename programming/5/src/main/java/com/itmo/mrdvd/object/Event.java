package com.itmo.mrdvd.object;

import org.apache.commons.lang3.math.NumberUtils;

public class Event implements Validatable, Comparable<Event> {
  private Long id;
  private String name;
  private String description;
  private EventType eventType;

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

  public static class EventParser {
    public static Long parseId(String id) {
      long result = NumberUtils.toLong(id);
      return (result == 0 ? null : result);
    }

    public static EventType parseType(String type) {
      for (EventType obj : EventType.values()) {
        if (type.equalsIgnoreCase(obj.name())) {
          return obj;
        }
      }
      return null;
    }
  }

  @Override
  public boolean isValid() {
    return EventValidator.validateId(getId())
        && EventValidator.validateName(getName())
        && EventValidator.validateDescription(getDescription())
        && EventValidator.validateEventType(getEventType());
  }

  public int setId(Long id) {
    if (EventValidator.validateId(id)) {
      this.id = id;
      return 0;
    }
    return -1;
  }

  public int setName(String name) {
    if (EventValidator.validateName(name)) {
      this.name = name;
      return 0;
    }
    return -1;
  }

  public int setDescription(String desc) {
    if (EventValidator.validateDescription(desc)) {
      this.description = desc;
      return 0;
    }
    return -1;
  }

  public int setEventType(EventType type) {
    if (EventValidator.validateEventType(type)) {
      this.eventType = type;
      return 0;
    }
    return -1;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public EventType getEventType() {
    return eventType;
  }

  @Override
  public String toString() {
    String s = "";
    s += String.format("ID: %d\n", getId());
    s += String.format("НАЗВАНИЕ: %s\n", getName());
    s += String.format("ОПИСАНИЕ: %s\n", getDescription());
    s += String.format("ТИП МЕРОПРИЯТИЯ: %s\n", getEventType());
    return s;
  }

  @Override
  public int compareTo(Event other) {
    return this.getId().compareTo(other.getId());
  }
}
