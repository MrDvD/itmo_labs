package com.itmo.mrdvd.builder.examples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.builder.InteractiveBuilder;
import com.itmo.mrdvd.builder.InteractiveObjectBuilder;
import com.itmo.mrdvd.builder.InteractiveObjectBuilder.UserInteractor;
import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.IntInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class TicketBuilder extends InteractiveObjectBuilder<Ticket> {

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

    private <T extends IntInputDevice & EnumInputDevice> void initSetters(CoordinatesBuilder coordBuild, EventBuilder eventBuild, T in) {
      addInteractiveSetter(Ticket::setName, String.class, new UserInteractor<String>("Название билета", in::read, "[ERROR] Неправильный формат ввода: название не должно быть пустым."), TicketValidator::validateName);
      addInteractiveBuilder(coordBuild, Ticket::setCoordinates, Coordinates.class);
      addInteractiveSetter(Ticket::setPrice, Integer.class, new UserInteractor<Integer>("Стоимость билета", () -> { Optional<Integer> res = in.readInt(); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: введите натуральное число.", "в у.е."), TicketValidator::validatePrice);
      String[] options = new String[TicketType.values().length];
      for (int i = 0; i < TicketType.values().length; i++) {
        options[i] = TicketType.values()[i].toString();
      }
      addInteractiveSetter(Ticket::setType, TicketType.class, new UserInteractor<Enum<TicketType>>("Тип билета", () -> { Optional<Enum<TicketType>> res = in.readEnum(TicketType.class); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: указанный тип билета не найден.", List.of(options)), TicketValidator::validateType);
      addInteractiveBuilder(eventBuild, Ticket::setEvent, Event.class);
    }

   public <T extends IntInputDevice & EnumInputDevice> TicketBuilder(CoordinatesBuilder coordBuild, EventBuilder eventBuild, T in, OutputDevice out) {
      super(new Ticket(), out);
      initSetters(coordBuild, eventBuild, in);
   }
   
   public <T extends IntInputDevice & EnumInputDevice> TicketBuilder(CoordinatesBuilder coordBuild, EventBuilder eventBuild, T in, OutputDevice out, List<Interactor<?>> interactors, List<TypedBiConsumer<Ticket,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders) {
      super(new Ticket(), out, interactors, setters, objects, validators, builders);
      initSetters(coordBuild, eventBuild, in);
   }

   @Override
   public Optional<Ticket> build() {
      return build(LocalDateTime.now());
   }

   public Optional<Ticket> build(LocalDateTime creationDate) {
      attr(Ticket::setCreationDate, creationDate, LocalDateTime.class, TicketValidator::validateCreationDate);
      return super.build();
   }
}
