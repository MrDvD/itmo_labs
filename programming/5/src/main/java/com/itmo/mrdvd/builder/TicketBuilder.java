package com.itmo.mrdvd.builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.builder.functionals.IndexedFunction;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.IntInputDevice;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class TicketBuilder extends InteractiveBuilder<Ticket> {
   private LocalDateTime creationDate = null;

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
      addInteractiveBuilder(coordBuild);
      attr(Ticket::setCreationDate, creationDate, LocalDateTime.class, TicketValidator::validateCreationDate);
      addInteractiveSetter(Ticket::setPrice, Integer.class, new UserInteractor<Integer>("Стоимость билета", () -> { Optional<Integer> res = in.readInt(); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: введите натуральное число.", "в у.е."), TicketValidator::validatePrice);
      String[] options = new String[TicketType.values().length];
      for (int i = 0; i < TicketType.values().length; i++) {
        options[i] = TicketType.values()[i].toString();
      }
      addInteractiveSetter(Ticket::setType, TicketType.class, new UserInteractor<Enum<TicketType>>("Тип билета", () -> { Optional<Enum<TicketType>> res = in.readEnum(TicketType.class); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: указанный тип билета не найден.", List.of(options)), TicketValidator::validateType);
      addInteractiveBuilder(eventBuild);
    }

   public <T extends IntInputDevice & EnumInputDevice> TicketBuilder(CoordinatesBuilder coordBuild, EventBuilder eventBuild, T in, OutputDevice out) {
      super(new Ticket(), out);
      initSetters(coordBuild, eventBuild, in);
   }
   
   public <T extends IntInputDevice & EnumInputDevice> TicketBuilder(CoordinatesBuilder coordBuild, EventBuilder eventBuild, T in, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<Ticket,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<IndexedFunction<ProcessStatus>> methods) {
      super(new Ticket(), out, interactors, setters, objects, validators, builders, methods);
      initSetters(coordBuild, eventBuild, in);
   }

   public Optional<Ticket> build(LocalDateTime creationDate) {
      this.creationDate = creationDate;
      return super.build();
   }
}
