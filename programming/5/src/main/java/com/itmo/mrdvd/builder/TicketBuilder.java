package com.itmo.mrdvd.builder;

import java.time.LocalDateTime;
import java.util.List;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.device.input.IntInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class TicketBuilder extends InteractiveBuilder<Ticket> {
   public static class TicketValidator {
       public static boolean validateCreationDate(LocalDateTime creationDate) {
        return creationDate != null;
      }
  
      public static boolean validateName(String name) {
        return name != null && !name.isBlank();
      }
  
      public static boolean validatePrice(Integer cost) {
        return cost > 0;
      }
  
      public static boolean validateType(TicketType type) {
        return type != null;
      }
    }

    private void initSetters(IntInputDevice inInt, FloatInputDevice inFloat, EnumInputDevice inEnum, LocalDateTime creationDate) {
      attr(Ticket::setCreationDate, creationDate, LocalDateTime.class, TicketValidator::validateCreationDate);
      addInteractiveSetter(Ticket::setName, String.class, new UserInteractor<String>("Название билета", inFloat::read), TicketValidator::validateName);
      addInteractiveBuilder(new CoordinatesBuilder(), Coordinates.class, new UserInteractor<Coordinates>("Координаты", inFloat::readCoordinates), TicketValidator::validateCoordinates);
      addInteractiveSetter(Ticket::setPrice, Integer.class, new UserInteractor<Integer>("Стоимость билета", inInt::readInt), TicketValidator::validatePrice);
      String[] options = new String[TicketType.values().length];
      for (int i = 0; i < TicketType.values().length; i++) {
        options[i] = TicketType.values()[i].toString();
      }
      addInteractiveSetter(Ticket::setType, TicketType.class, new UserInteractor<Enum<TicketType>>("Тип билета", () -> inEnum.readEnum(TicketType.class), List.of(options)), TicketValidator::validateType);
      addInteractiveBuilder(Ticket::setEvent, Event.class, new UserInteractor<Event>("Мероприятие", inFloat::readEvent), TicketValidator::validateEvent);
    }

   public TicketBuilder(Ticket rawObject, IntInputDevice inInt, FloatInputDevice inFloat, EnumInputDevice inEnum, LocalDateTime creationDate, OutputDevice out) {
      super(rawObject, out);
      initSetters(inInt, inFloat, inEnum, creationDate);
   }
   
   public TicketBuilder(Ticket rawObject, IntInputDevice inInt, FloatInputDevice inFloat, EnumInputDevice inEnum, LocalDateTime creationDate, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<Ticket,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<IndexedFunction<Boolean>> methods) {
      super(rawObject, out, interactors, setters, objects, validators, builders, methods);
      initSetters(inInt, inFloat, inEnum, creationDate);
   }
}
