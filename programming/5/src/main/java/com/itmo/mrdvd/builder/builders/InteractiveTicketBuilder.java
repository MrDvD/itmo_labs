package com.itmo.mrdvd.builder.builders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.UserInteractor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.builder.validators.TicketValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.device.input.IntEnumInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class InteractiveTicketBuilder extends InteractiveObjectBuilder<Ticket, IntEnumInputDevice> {
  private final InteractiveBuilder<Coordinates, ?> coordBuild;
  private final InteractiveBuilder<Event, ?> eventBuild;
  
  private InteractiveTicketBuilder init() {
    of(Ticket::new);
    addInteractiveSetter(
        Ticket::setName,
        String.class,
        new UserInteractor<>(
            "Название билета",
            InputDevice::read,
            "[ERROR] Неправильный формат ввода: название не должно быть пустым."),
        TicketValidator::validateName);
    addInteractiveBuilder(coordBuild, Ticket::setCoordinates, Coordinates.class);
    addInteractiveSetter(
        Ticket::setPrice,
        Integer.class,
        new UserInteractor<>(
            "Стоимость билета",
            (IntEnumInputDevice x) -> {
              Optional<Integer> res = x.readInt();
              x.skipLine();
              return res;
            },
            "[ERROR] Неправильный формат ввода: введите натуральное число.",
            "в у.е."),
        TicketValidator::validatePrice);
    String[] options = new String[TicketType.values().length];
    for (int i = 0; i < TicketType.values().length; i++) {
      options[i] = TicketType.values()[i].toString();
    }
    addInteractiveSetter(
        Ticket::setType,
        TicketType.class,
        new UserInteractor<>(
            "Тип билета",
            (IntEnumInputDevice x) -> {
              Optional<Enum<TicketType>> res = x.readEnum(TicketType.class);
              x.skipLine();
              return res;
            },
            "[ERROR] Неправильный формат ввода: указанный тип билета не найден.",
            List.of(options)),
        TicketValidator::validateType);
    addInteractiveBuilder(eventBuild, Ticket::setEvent, Event.class);
    setFromMethod(
        Ticket::setCreationDate,
        LocalDateTime::now,
        LocalDateTime.class,
        TicketValidator::validateCreationDate);
    return this;
  }

  public InteractiveTicketBuilder(
      InteractiveBuilder<Coordinates, ?> coordBuild,
      InteractiveBuilder<Event, ?> eventBuild,
      IntEnumInputDevice in,
      OutputDevice out) {
    super(in, out);
    this.coordBuild = coordBuild;
    this.eventBuild = eventBuild;
    init();
  }

  public InteractiveTicketBuilder(
      InteractiveBuilder<Coordinates, ?> coordBuild,
      InteractiveBuilder<Event, ?> eventBuild,
      IntEnumInputDevice in,
      OutputDevice out,
      List<Interactor<?, IntEnumInputDevice>> interactors,
      List<TypedBiConsumer<Ticket, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<InteractiveBuilder<?, ?>> builders) {
    super(in, out, interactors, setters, objects, methods, validators, builders);
    this.coordBuild = coordBuild;
    this.eventBuild = eventBuild;
    init();
  }

  @Override
  public InteractiveTicketBuilder setIn(IntEnumInputDevice in) {
    return new InteractiveTicketBuilder(coordBuild, eventBuild, in, out).init();
  }
}
