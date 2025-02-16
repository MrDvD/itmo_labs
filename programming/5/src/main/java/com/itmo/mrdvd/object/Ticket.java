package com.itmo.mrdvd.object;

import java.time.LocalDateTime;

public class Ticket implements Validatable {
   private Long id;
   private String name;
   private Coordinates coordinates;
   private LocalDateTime creationDate;
   private int price;
   private TicketType type;
   private Event event;
   // public Ticket(Long id, String name, Coordinates coords, LocalDateTime date, int price, TicketType type, Event event) {
   //    this.id = id;
   //    this.name = name;
   //    this.coordinates = coords;
   //    this.creationDate = date;
   //    this.price = price;
   //    this.type = type;
   //    this.event = event;
   //    // GENERATE ID
   // }
   public static class TicketValidator {
      public static boolean validateName(String name) {
         if (name == null) {
            return false;
         }
         return true;
      }
      public static boolean validateCoordinates(Coordinates coords) {
         return coords != null && coords.isValid();
      }
      public static boolean validatePrice(int cost) {
         return cost > 0;
      }
   }
   @Override
   public boolean isValid() {
      return TicketValidator.validateName(getName()) &&
             TicketValidator.validateCoordinates(getCoordinates());
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
   public int setPrice(String price) {
      // parse
      return setPrice(price);
   }
   public int setType(TicketType type) {
      this.type = type;
      return 0;
   }
   public int setType(String type) {
      // parse
      return setType(type);
   }
   public int setEvent(Event event) {
      this.event = event;
      return 0;
   }
   public int setEvent(String type) {
      // parse
      return setEvent(type);
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
}