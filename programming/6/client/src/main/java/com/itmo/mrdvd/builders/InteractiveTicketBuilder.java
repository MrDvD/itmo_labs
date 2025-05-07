package com.itmo.mrdvd.builders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.builder.builders.InteractiveObjectBuilder;
import com.itmo.mrdvd.builder.interactors.Interactor;
import com.itmo.mrdvd.builder.interactors.UserInteractor;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;
import com.itmo.mrdvd.service.shell.AbstractShell;
import com.itmo.mrdvd.validators.TicketValidator;

public class InteractiveTicketBuilder extends InteractiveObjectBuilder<Ticket> {
  public InteractiveTicketBuilder(
      InteractiveBuilder<Coordinates> coordBuild,
      InteractiveBuilder<Event> eventBuild,
      AbstractShell shell) {
    this(coordBuild, eventBuild, shell, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public InteractiveTicketBuilder(
      InteractiveBuilder<Coordinates> coordBuild,
      InteractiveBuilder<Event> eventBuild,
      AbstractShell shell,
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveBuilder<?>> builders) {
    super(interactors, setters, methods, validators, builders);
    of(Ticket::new);
    addInteractiveSetter(
        Ticket::setName,
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
    addInteractiveBuilder(Ticket::setCoordinates, coordBuild);
    addInteractiveSetter(
        Ticket::setPrice,
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
    addInteractiveSetter(
        Ticket::setType,
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
    addInteractiveBuilder(Ticket::setEvent, eventBuild);
    set(Ticket::setCreationDate,
        LocalDateTime::now,
        TicketValidator::validateCreationDate);
  }
}
