package com.itmo.mrdvd.object;

import com.itmo.mrdvd.collection.HavingId;

public class Event implements HavingId, Comparable<Event> {
  private Long id;
  private String name;
  private String description;
  private EventType eventType;

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setDescription(String desc) {
    this.description = desc;
  }

  public void setType(EventType type) {
    this.eventType = type;
  }

  @Override
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public EventType getType() {
    return eventType;
  }

  @Override
  public String toString() {
    String s = "";
    s += String.format("ID: %d\n", getId());
    s += String.format("НАЗВАНИЕ: %s\n", getName());
    s += String.format("ОПИСАНИЕ: %s\n", getDescription());
    s += String.format("ТИП МЕРОПРИЯТИЯ: %s\n", getType());
    return s;
  }

  @Override
  public int compareTo(Event other) {
    return this.getId().compareTo(other.getId());
  }
}
