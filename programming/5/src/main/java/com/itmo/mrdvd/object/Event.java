package com.itmo.mrdvd.object;

public class Event implements Validatable {
   private Long id;
   private String name;
   private String description;
   private EventType eventType;
   public static class EventValidator {
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
      return EventValidator.validateName(getName()) &&
             EventValidator.validateDescription(getDescription()) &&
             EventValidator.validateEventType(getEventType());
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
}