package com.itmo.mrdvd.builder.updaters;

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
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class InteractiveTicketUpdater extends InteractiveObjectUpdater<Ticket> {
  private <T extends IntInputDevice & EnumInputDevice> void init(
      InteractiveUpdater<Coordinates> coordUpdate, InteractiveUpdater<Event> eventUpdate, T in) {
    addInteractiveChange(
        Ticket::setName,
        Ticket::getName,
        String.class,
        new UserInteractor<String>(
            "Название билета",
            in::read,
            "[ERROR] Неправильный формат ввода: название не должно быть пустым."),
        TicketValidator::validateName);
    addInteractiveUpdater(
        coordUpdate, Ticket::setCoordinates, Ticket::getCoordinates, Coordinates.class);
    addInteractiveChange(
        Ticket::setPrice,
        Ticket::getPrice,
        Integer.class,
        new UserInteractor<Integer>(
            "Стоимость билета",
            () -> {
              Optional<Integer> res = in.readInt();
              in.skipLine();
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
        new UserInteractor<Enum<TicketType>>(
            "Тип билета",
            () -> {
              Optional<Enum<TicketType>> res = in.readEnum(TicketType.class);
              in.skipLine();
              return res;
            },
            "[ERROR] Неправильный формат ввода: указанный тип билета не найден.",
            List.of(options)),
        TicketValidator::validateType);
    addInteractiveUpdater(eventUpdate, Ticket::setEvent, Ticket::getEvent, Event.class);
  }

  public <T extends IntInputDevice & EnumInputDevice> InteractiveTicketUpdater(
      InteractiveUpdater<Coordinates> coordUpdate,
      InteractiveUpdater<Event> eventUpdate,
      T in,
      OutputDevice out) {
    super(out);
    init(coordUpdate, eventUpdate, in);
  }

  public <T extends IntInputDevice & EnumInputDevice> InteractiveTicketUpdater(
      InteractiveUpdater<Coordinates> coordUpdate,
      InteractiveUpdater<Event> eventUpdate,
      T in,
      OutputDevice out,
      List<Interactor<?>> interactors,
      List<TypedBiConsumer<Ticket, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<Function<Ticket, ?>> getters,
      List<InteractiveUpdater> updaters) {
    super(out, interactors, setters, objects, methods, validators, getters, updaters);
    init(coordUpdate, eventUpdate, in);
  }
}
