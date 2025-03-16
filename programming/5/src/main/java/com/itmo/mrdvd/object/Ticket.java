package com.itmo.mrdvd.object;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.itmo.mrdvd.collection.HavingId;

public class Ticket implements HavingId {
  private Long id;
  private String name;
  private Coordinates coordinates;
  private LocalDateTime creationDate;
  private int price;
  private TicketType type;
  private Event event;

  @Override
  public void setId(Long id) {
   this.id = id;
  }

  public void setCreationDate(LocalDateTime date) {
   this.creationDate = date;
}

public void setName(String name) {
   this.name = name;
}

public void setCoordinates(Coordinates coords) {
   this.coordinates = coords;
}

public void setPrice(int price) {
   this.price = price;
}

public void setType(TicketType type) {
   this.type = type;
}

public void setEvent(Event event) {
   this.event = event;
}

@Override
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
