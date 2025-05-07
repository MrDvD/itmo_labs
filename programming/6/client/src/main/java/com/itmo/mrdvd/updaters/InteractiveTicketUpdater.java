package com.itmo.mrdvd.updaters;

import com.itmo.mrdvd.builder.interactors.Interactor;
import com.itmo.mrdvd.builder.interactors.UserInteractor;
import com.itmo.mrdvd.builder.updaters.InteractiveObjectUpdater;
import com.itmo.mrdvd.builder.updaters.InteractiveUpdater;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;
import com.itmo.mrdvd.service.shell.AbstractShell;
import com.itmo.mrdvd.validators.TicketValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveTicketUpdater extends InteractiveObjectUpdater<Ticket> {
  public InteractiveTicketUpdater(
      InteractiveUpdater<Coordinates> coordUpdate,
      InteractiveUpdater<Event> eventUpdate,
      AbstractShell shell) {
    this(
        coordUpdate,
        eventUpdate,
        shell,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveTicketUpdater(
      InteractiveUpdater<Coordinates> coordUpdate,
      InteractiveUpdater<Event> eventUpdate,
      AbstractShell shell,
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Function<Ticket, ?>> getters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveUpdater> updaters) {
    super(interactors, setters, getters, methods, validators, updaters);
    addInteractiveChange(
        Ticket::setName,
        Ticket::getName,
        new UserInteractor<>(
            "Название билета",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<String> res = x.read();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: название не должно быть пустым.\n"),
        TicketValidator::validateName);
    addInteractiveUpdater(Ticket::setCoordinates, Ticket::getCoordinates, coordUpdate);
    addInteractiveChange(
        Ticket::setPrice,
        Ticket::getPrice,
        new UserInteractor<>(
            "Стоимость билета",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<Integer> res = x.readInt();
              x.skipLine();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: введите натуральное число.\n",
            "в у.е."),
        TicketValidator::validatePrice);
    String[] options = new String[TicketType.values().length];
    for (int i = 0; i < TicketType.values().length; i++) {
      options[i] = TicketType.values()[i].toString();
    }
    addInteractiveChange(
        Ticket::setType,
        Ticket::getType,
        new UserInteractor<>(
            "Тип билета",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<Enum<TicketType>> res = x.readEnum(TicketType.class);
              x.skipLine();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: указанный тип билета не найден.\n",
            List.of(options)),
        TicketValidator::validateType);
    addInteractiveUpdater(Ticket::setEvent, Ticket::getEvent, eventUpdate);
  }
}
