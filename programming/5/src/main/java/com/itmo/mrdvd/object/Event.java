package com.itmo.mrdvd.object;

public class Event implements Validatable {
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
      return EventValidator.validateId(getId()) &&
             EventValidator.validateName(getName()) &&
             EventValidator.validateDescription(getDescription()) &&
             EventValidator.validateEventType(getEventType());
   }
   public int setId(Long id) {
      return setId(id, true);
   }
   public int setId(Long id, boolean validate) {
      if (!validate || EventValidator.validateId(id)) {
         this.id = id;
         return 0;
      }
      return -1;
   }
   public int setName(String name) {
      return setName(name, true);
   }
   public int setName(String name, boolean validate) {
      if (!validate || EventValidator.validateName(name)) {
         this.name = name;
         return 0;
      }
      return -1;
   }
   public int setDescription(String desc) {
      return setDescription(desc, true);
   }
   public int setDescription(String desc, boolean validate) {
      if (!validate || EventValidator.validateDescription(desc)) {
         this.description = desc;
         return 0;
      }
      return -1;
   }
   public int setEventType(EventType type) {
      return setEventType(type, true);
   }
   public int setEventType(EventType type, boolean validate) {
      if (!validate || EventValidator.validateEventType(type)) {
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
}