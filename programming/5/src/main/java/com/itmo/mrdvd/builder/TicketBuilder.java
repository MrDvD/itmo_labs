package com.itmo.mrdvd.builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class TicketBuilder extends InteractiveBuilder<Ticket> {
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
   public TicketBuilder(Ticket raw) {
      super(raw);
   }
   
   public TicketBuilder(Ticket raw, List<TypedBiConsumer<Ticket,?>> setters, List<Object> objects, List<Function<Object, Boolean>> validators) {
      super(raw, setters, objects, validators);
   }
}
