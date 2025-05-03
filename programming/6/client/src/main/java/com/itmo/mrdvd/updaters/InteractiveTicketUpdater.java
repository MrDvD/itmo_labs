package com.itmo.mrdvd.updaters;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.UserInteractor;
import com.itmo.mrdvd.builder.updaters.InteractiveObjectUpdater;
import com.itmo.mrdvd.builder.updaters.InteractiveUpdater;
import com.itmo.mrdvd.builder.validators.TicketValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveTicketUpdater extends InteractiveObjectUpdater<Ticket, DataInputDevice> {
  private final InteractiveUpdater<Coordinates, FloatInputDevice> coordUpdate;
  private final InteractiveUpdater<Event, EnumInputDevice> eventUpdate;

  private void init() {
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
            (x) -> {
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
      InteractiveUpdater<Coordinates, FloatInputDevice> coordUpdate,
      InteractiveUpdater<Event, EnumInputDevice> eventUpdate,
      DataInputDevice in,
      OutputDevice out) {
    super(in, out);
    this.coordUpdate = coordUpdate;
    this.eventUpdate = eventUpdate;
    init();
  }

  public InteractiveTicketUpdater(
      InteractiveUpdater<Coordinates, FloatInputDevice> coordUpdate,
      InteractiveUpdater<Event, EnumInputDevice> eventUpdate,
      DataInputDevice in,
      OutputDevice out,
      List<Interactor<?, DataInputDevice>> interactors,
      List<BiConsumer> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<Function<Ticket, ?>> getters,
      List<InteractiveUpdater> updaters) {
    super(in, out, interactors, setters, objects, methods, validators, getters, updaters);
    this.coordUpdate = coordUpdate;
    this.eventUpdate = eventUpdate;
    init();
  }

  @Override
  public InteractiveTicketUpdater setIn(DataInputDevice in) {
    InteractiveUpdater newCoords = this.coordUpdate.setIn(in);
    InteractiveUpdater newEvent = this.eventUpdate.setIn(in);
    return new InteractiveTicketUpdater(newCoords, newEvent, in, out);
  }
}
