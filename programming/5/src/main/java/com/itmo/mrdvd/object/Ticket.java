package com.itmo.mrdvd.object;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.math.NumberUtils;

public class Ticket implements Validatable {
  private Long id;
  private String name;
  private Coordinates coordinates;
  private LocalDateTime creationDate;
  private int price;
  private TicketType type;
  private Event event;

  public static class TicketValidator {
    public static boolean validateId(Long id) {
      return id != null && id > 0;
    }

    public static boolean validateCreationDate(LocalDateTime creationDate) {
      return creationDate != null;
    }

    public static boolean validateName(String name) {
      return name != null && !name.isBlank();
    }

    public static boolean validateCoordinates(Coordinates coords) {
      return coords != null && coords.isValid();
    }

    public static boolean validatePrice(int cost) {
      return cost > 0;
    }

    public static boolean validateType(TicketType type) {
      return type != null;
    }

    public static boolean validateEvent(Event event) {
      return event != null && event.isValid();
    }
  }

  public static class TicketParser {
    public static Long parseId(String id) {
      long result = NumberUtils.toLong(id);
      return (result == 0 ? null : result);
    }

    public static int parsePrice(String price) {
      return NumberUtils.toInt(price, -1);
    }

    public static TicketType parseType(String type) {
      for (TicketType obj : TicketType.values()) {
        if (type.equalsIgnoreCase(obj.name())) {
          return obj;
        }
      }
      return null;
    }
  }

  @Override
  public boolean isValid() {
    return TicketValidator.validateId(getId())
        && TicketValidator.validateCreationDate(getCreationDate())
        && TicketValidator.validateName(getName())
        && TicketValidator.validateCoordinates(getCoordinates())
        && TicketValidator.validatePrice(getPrice())
        && TicketValidator.validateType(getType())
        && TicketValidator.validateEvent(getEvent());
  }

  public int setId(Long id) {
    if (TicketValidator.validateId(id)) {
      this.id = id;
      return 0;
    }
    return -1;
  }

  public int setCreationDate(LocalDateTime date) {
    if (TicketValidator.validateCreationDate(date)) {
      this.creationDate = date;
      return 0;
    }
    return -1;
  }

  public int setName(String name) {
    if (TicketValidator.validateName(name)) {
      this.name = name;
      return 0;
    }
    return -1;
  }

  public int setCoordinates(Coordinates coords) {
    if (TicketValidator.validateCoordinates(coords)) {
      this.coordinates = coords;
      return 0;
    }
    return -1;
  }

  public int setPrice(int price) {
    if (TicketValidator.validatePrice(price)) {
      this.price = price;
      return 0;
    }
    return -1;
  }

  public int setType(TicketType type) {
    if (TicketValidator.validateType(type)) {
      this.type = type;
      return 0;
    }
    return -1;
  }

  public int setEvent(Event event) {
    if (TicketValidator.validateEvent(event)) {
      this.event = event;
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

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public int getPrice() {
    return price;
  }

  public TicketType getType() {
    return type;
  }

  public Event getEvent() {
    return event;
  }

  @Override
  public String toString() {
    String s = "";
    s += "= = = = = = = = = = = = = = = =\n";
    s += String.format("ID: %d\n", getId());
    s += String.format("НАЗВАНИЕ БИЛЕТА: %s\n", getName());
    s += String.format("КООРДИНАТЫ: %s\n", getCoordinates());
    s +=
        String.format(
            "ДАТА СОЗДАНИЯ: %s\n",
            getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    s += String.format("СТОИМОСТЬ: %d у.е.\n", getPrice());
    s += String.format("ТИП БИЛЕТА: %s\n", getType());
    s += "######### МЕРОПРИЯТИЕ #########\n";
    s += getEvent().toString();
    s += "- - - - - - - - - - - - - - - -";
    return s;
  }
}
