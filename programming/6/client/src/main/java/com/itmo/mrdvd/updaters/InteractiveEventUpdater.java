package com.itmo.mrdvd.updaters;

import com.itmo.mrdvd.builder.interactors.Interactor;
import com.itmo.mrdvd.builder.interactors.UserInteractor;
import com.itmo.mrdvd.builder.updaters.InteractiveObjectUpdater;
import com.itmo.mrdvd.builder.updaters.InteractiveUpdater;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;
import com.itmo.mrdvd.service.shell.AbstractShell;
import com.itmo.mrdvd.validators.EventValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveEventUpdater extends InteractiveObjectUpdater<Event> {
  public InteractiveEventUpdater(AbstractShell shell) {
    this(
        shell,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveEventUpdater(
      AbstractShell shell,
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Function<Event, ?>> getters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveUpdater> updaters) {
    super(interactors, setters, getters, methods, validators, updaters);
    addInteractiveChange(
        Event::setName,
        Event::getName,
        new UserInteractor<>(
            "Имя мероприятия",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<String> res = x.read();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: имя не должно быть пустым.\n"),
        EventValidator::validateName);
    addInteractiveChange(
        Event::setDescription,
        Event::getName,
        new UserInteractor<>(
            "Описание мероприятия",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<String> res = x.read();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: описание не должно быть пустым и превышать длину в 1190 символов.\n"),
        EventValidator::validateDescription);
    String[] options = new String[EventType.values().length];
    for (int i = 0; i < EventType.values().length; i++) {
      options[i] = EventType.values()[i].toString();
    }
    addInteractiveChange(
        Event::setType,
        Event::getType,
        new UserInteractor<>(
            "Тип мероприятия",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<Enum<EventType>> res = x.readEnum(EventType.class);
              x.skipLine();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.\n",
            List.of(options)),
        EventValidator::validateType);
  }
}
