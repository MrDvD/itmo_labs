package com.itmo.mrdvd.builder.updaters;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.UserInteractor;
import com.itmo.mrdvd.builder.validators.EventValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveEventUpdater extends InteractiveObjectUpdater<Event, EnumInputDevice> {
  private void init() {
    addInteractiveChange(
        Event::setName,
        Event::getName,
        String.class,
        new UserInteractor<>(
            "Имя мероприятия",
            InputDevice::read,
            "[ERROR] Неправильный формат ввода: имя не должно быть пустым."),
        EventValidator::validateName);
    addInteractiveChange(
        Event::setDescription,
        Event::getName,
        String.class,
        new UserInteractor<>(
            "Описание мероприятия",
            InputDevice::read,
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
        new UserInteractor<>(
            "Тип мероприятия",
            (EnumInputDevice x) -> {
              Optional<Enum<EventType>> result = x.readEnum(EventType.class);
              x.skipLine();
              return result;
            },
            "[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.",
            List.of(options)),
        EventValidator::validateType);
  }

  public InteractiveEventUpdater(EnumInputDevice in, OutputDevice out) {
    super(in, out);
    init();
  }

  public InteractiveEventUpdater(
      EnumInputDevice in,
      OutputDevice out,
      List<Interactor<?, EnumInputDevice>> interactors,
      List<BiConsumer> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<Function<Event, ?>> getters,
      List<InteractiveUpdater> updaters) {
    super(in, out, interactors, setters, objects, methods, validators, getters, updaters);
    init();
  }

  @Override
  public InteractiveEventUpdater setIn(EnumInputDevice in) {
    return new InteractiveEventUpdater(in, out);
  }
}
