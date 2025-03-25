package com.itmo.mrdvd.builder.updaters;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.builder.validators.EventValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class InteractiveEventUpdater extends InteractiveObjectUpdater<Event> {
  private void init(EnumInputDevice in) {
    addInteractiveChange(
        Event::setName,
        Event::getName,
        String.class,
        new UserInteractor<String>(
            "Имя мероприятия",
            in::read,
            "[ERROR] Неправильный формат ввода: имя не должно быть пустым."),
        EventValidator::validateName);
    addInteractiveChange(
        Event::setDescription,
        Event::getName,
        String.class,
        new UserInteractor<String>(
            "Описание мероприятия",
            in::read,
            "[ERROR] Неправильный формат ввода: описание не должно быть пустым и превышать длину в 1190 символов."),
        EventValidator::validateDescription);
    String[] options = new String[EventType.values().length];
    for (int i = 0; i < EventType.values().length; i++) {
      options[i] = EventType.values()[i].toString();
    }
    addInteractiveChange(
        Event::setType,
        Event::getType,
        EventType.class,
        new UserInteractor<Enum<EventType>>(
            "Тип мероприятия",
            () -> {
              Optional<Enum<EventType>> result = in.readEnum(EventType.class);
              in.skipLine();
              return result;
            },
            "[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.",
            List.of(options)),
        EventValidator::validateType);
  }

  public InteractiveEventUpdater(EnumInputDevice in, OutputDevice out) {
    super(out);
    init(in);
  }

  public InteractiveEventUpdater(
      EnumInputDevice in,
      OutputDevice out,
      List<Interactor<?>> interactors,
      List<TypedBiConsumer<Event, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<Function<Event, ?>> getters,
      List<InteractiveUpdater> updaters) {
    super(out, interactors, setters, objects, methods, validators, getters, updaters);
    init(in);
  }
}
