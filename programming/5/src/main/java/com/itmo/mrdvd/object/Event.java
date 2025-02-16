package com.itmo.mrdvd.object;

public class Event {
   private Long id;
   private String name;
   private String description;
   private EventType eventType;
   public Event(Long id, String name, String desc, EventType type) {
      this.id = id;
      this.name = name;
      this.description = desc;
      this.eventType = type;
   }
   public Long id() {
      return id;
   }
   public String name() {
      return name;
   }
   public String description() {
      return description;
   }
   public EventType eventType() {
      return eventType;
   }
}