package com.itmo.mrdvd.builder.updaters;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

public class InteractiveTicketUpdater extends InteractiveObjectUpdater<Ticket, IntEnumInputDevice> {
  private void init(
      InteractiveUpdater<Coordinates, ?> coordUpdate, InteractiveUpdater<Event, ?> eventUpdate) {
    addInteractiveChange(
        Ticket::setName,
        Ticket::getName,
        String.class,
        new UserInteractor<>(
            "Название билета",
            InputDevice::read,
            "[ERROR] Неправильный формат ввода: название не должно быть пустым."),
        TicketValidator::validateName);
    addInteractiveUpdater(
        coordUpdate, Ticket::setCoordinates, Ticket::getCoordinates, Coordinates.class);
    addInteractiveChange(
        Ticket::setPrice,
        Ticket::getPrice,
        Integer.class,
        new UserInteractor<>(
            "Стоимость билета",
            (x) -> {
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
    addInteractiveChange(
        Ticket::setType,
        Ticket::getType,
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
    addInteractiveUpdater(eventUpdate, Ticket::setEvent, Ticket::getEvent, Event.class);
  }

  public InteractiveTicketUpdater(
      InteractiveUpdater<Coordinates, ?> coordUpdate,
      InteractiveUpdater<Event, ?> eventUpdate,
      IntEnumInputDevice in,
      OutputDevice out) {
    super(in, out);
    init(coordUpdate, eventUpdate);
  }

  public InteractiveTicketUpdater(
      InteractiveUpdater<Coordinates,?> coordUpdate,
      InteractiveUpdater<Event,?> eventUpdate,
      IntEnumInputDevice in,
      OutputDevice out,
      List<Interactor<?, IntEnumInputDevice>> interactors,
      List<TypedBiConsumer<Ticket, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<Function<Ticket, ?>> getters,
      List<InteractiveUpdater> updaters) {
    super(in, out, interactors, setters, objects, methods, validators, getters, updaters);
    init(coordUpdate, eventUpdate);
  }
}
