package com.itmo.mrdvd.builder.examples;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.Builder;
import com.itmo.mrdvd.builder.InteractiveBuilder;
import com.itmo.mrdvd.builder.InteractiveObjectBuilder;
import com.itmo.mrdvd.builder.InteractiveUpdater;
import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.builder.validators.TicketValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.IntInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class InteractiveTicketBuilder extends InteractiveObjectBuilder<Ticket> {
    private <T extends IntInputDevice & EnumInputDevice> void init(InteractiveBuilder<Coordinates> coordBuild, InteractiveBuilder<Event> eventBuild, T in) {
      of(Ticket::new);
      addInteractiveSetter(Ticket::setName, String.class, new UserInteractor<String>("Название билета", in::read, "[ERROR] Неправильный формат ввода: название не должно быть пустым."), TicketValidator::validateName);
      addInteractiveBuilder(coordBuild, Ticket::setCoordinates, Coordinates.class);
      addInteractiveSetter(Ticket::setPrice, Integer.class, new UserInteractor<Integer>("Стоимость билета", () -> { Optional<Integer> res = in.readInt(); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: введите натуральное число.", "в у.е."), TicketValidator::validatePrice);
      String[] options = new String[TicketType.values().length];
      for (int i = 0; i < TicketType.values().length; i++) {
        options[i] = TicketType.values()[i].toString();
      }
      addInteractiveSetter(Ticket::setType, TicketType.class, new UserInteractor<Enum<TicketType>>("Тип билета", () -> { Optional<Enum<TicketType>> res = in.readEnum(TicketType.class); in.skipLine(); return res; }, "[ERROR] Неправильный формат ввода: указанный тип билета не найден.", List.of(options)), TicketValidator::validateType);
      addInteractiveBuilder(eventBuild, Ticket::setEvent, Event.class);
      attrFromMethod(Ticket::setCreationDate, LocalDateTime::now, LocalDateTime.class, TicketValidator::validateCreationDate);
    }

   public <T extends IntInputDevice & EnumInputDevice> InteractiveTicketBuilder(InteractiveBuilder<Coordinates> coordBuild, InteractiveBuilder<Event> eventBuild, T in, OutputDevice out) {
      super(out);
      init(coordBuild, eventBuild, in);
   }
   
   public <T extends IntInputDevice & EnumInputDevice> InteractiveTicketBuilder(InteractiveBuilder<Coordinates> coordBuild, InteractiveBuilder<Event> eventBuild, T in, OutputDevice out, List<Interactor<?>> interactors, List<TypedBiConsumer<Ticket,?>> setters, List<Object> objects, List<Supplier<?>> methods, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<Function<Ticket,?>> getters, List<InteractiveUpdater> updaters) {
      super(out, interactors, setters, objects, methods, validators, builders, getters, updaters);
      init(coordBuild, eventBuild, in);
   }
}
