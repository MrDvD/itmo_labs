package com.itmo.mrdvd.builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
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

    private void initSetters(CoordinatesBuilder coordBuild, EventBuilder eventBuild, IntInputDevice inInt, FloatInputDevice inFloat, EnumInputDevice inEnum) {
      addInteractiveSetter(Ticket::setName, String.class, new UserInteractor<String>("Название билета", inFloat::read, "[ERROR] Неправильный формат ввода: название не должно быть пустым."), TicketValidator::validateName);
      addInteractiveBuilder(coordBuild);
      attr(Ticket::setCreationDate, creationDate, LocalDateTime.class, TicketValidator::validateCreationDate);
      addInteractiveSetter(Ticket::setPrice, Integer.class, new UserInteractor<Integer>("Стоимость билета", inInt::readInt, "[ERROR] Неправильный формат ввода: введите натуральное число.", "в у.е."), TicketValidator::validatePrice);
      String[] options = new String[TicketType.values().length];
      for (int i = 0; i < TicketType.values().length; i++) {
        options[i] = TicketType.values()[i].toString();
      }
      addInteractiveSetter(Ticket::setType, TicketType.class, new UserInteractor<Enum<TicketType>>("Тип билета", () -> inEnum.readEnum(TicketType.class), "[ERROR] Неправильный формат ввода: указанный тип билета не найден.", List.of(options)), TicketValidator::validateType);
      addInteractiveBuilder(eventBuild);
    }

   public TicketBuilder(CoordinatesBuilder coordBuild, EventBuilder eventBuild, IntInputDevice inInt, FloatInputDevice inFloat, EnumInputDevice inEnum, OutputDevice out) {
      super(new Ticket(), out);
      initSetters(coordBuild, eventBuild, inInt, inFloat, inEnum);
   }
   
   public TicketBuilder(CoordinatesBuilder coordBuild, EventBuilder eventBuild, IntInputDevice inInt, FloatInputDevice inFloat, EnumInputDevice inEnum, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<Ticket,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<IndexedFunction<ProcessStatus>> methods) {
      super(new Ticket(), out, interactors, setters, objects, validators, builders, methods);
      initSetters(coordBuild, eventBuild, inInt, inFloat, inEnum);
   }

   public Optional<Ticket> build(LocalDateTime creationDate) {
      this.creationDate = creationDate;
      return super.build();
   }
}
