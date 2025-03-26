package com.itmo.mrdvd.builder.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.UserInteractor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.builder.validators.EventValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.InputDevice;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;

public class InteractiveEventBuilder extends InteractiveObjectBuilder<Event> {
  private void init(Supplier<EnumInputDevice> in) {
    of(Event::new);
    addInteractiveSetter(
        Event::setName,
        String.class,
        new UserInteractor<>(
            "Имя мероприятия",
            InputDevice::read,
            "[ERROR] Неправильный формат ввода: имя не должно быть пустым."),
        EventValidator::validateName);
    addInteractiveSetter(
        Event::setDescription,
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
    addInteractiveSetter(
        Event::setType,
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

  public InteractiveEventBuilder(Supplier<EnumInputDevice> in, OutputDevice out) {
    super(out);
    init(in);
  }

  public InteractiveEventBuilder(
      Supplier<EnumInputDevice> in,
      OutputDevice out,
      List<Interactor<?, ?>> interactors,
      List<TypedBiConsumer<Event, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<InteractiveBuilder<?>> builders) {
    super(out, interactors, setters, objects, methods, validators, builders);
    init(in);
  }
}
