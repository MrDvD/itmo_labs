package com.itmo.mrdvd.builders;

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
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;
import com.itmo.mrdvd.service.shell.AbstractShell;
import com.itmo.mrdvd.validators.EventValidator;

public class InteractiveEventBuilder extends InteractiveObjectBuilder<Event> {
  public InteractiveEventBuilder(AbstractShell shell) {
    this(shell, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
  }

  public InteractiveEventBuilder(
      AbstractShell shell,
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveBuilder<?>> builders) {
    super(interactors, setters, methods, validators, builders);
    of(Event::new);
    addInteractiveSetter(
        Event::setName,
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
    addInteractiveSetter(
        Event::setDescription,
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
    addInteractiveSetter(
        Event::setType,
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
