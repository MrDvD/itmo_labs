package com.itmo.mrdvd.builder.examples;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.InteractiveBuilder;
import com.itmo.mrdvd.builder.InteractiveObjectBuilder;
import com.itmo.mrdvd.builder.InteractiveUpdater;
import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;

public class EventUpdater extends InteractiveObjectBuilder<Event> {
   public static class EventValidator {
    public static boolean validateName(String name) {
      return name != null && !name.isBlank();
    }

    public static boolean validateDescription(String description) {
      return description != null && !description.isBlank() && description.length() <= 1190;
    }

    public static boolean validateType(EventType type) {
      return type != null;
    }
  }

  private void init(EnumInputDevice in) {
      of(Event::new);
      addInteractiveSetter(Event::setName, Event::getName, String.class, new UserInteractor<String>("Имя мероприятия", in::read, "[ERROR] Неправильный формат ввода: имя не должно быть пустым."), EventValidator::validateName);
      addInteractiveSetter(Event::setDescription, Event::getName, String.class, new UserInteractor<String>("Описание мероприятия", in::read, "[ERROR] Неправильный формат ввода: описание не должно быть пустым и превышать длину в 1190 символов."), EventValidator::validateDescription);
      String[] options = new String[EventType.values().length];
      for (int i = 0; i < EventType.values().length; i++) {
        options[i] = EventType.values()[i].toString();
      }
      addInteractiveSetter(Event::setType, Event::getType, EventType.class, new UserInteractor<Enum<EventType>>("Тип мероприятия", () -> { Optional<Enum<EventType>> result = in.readEnum(EventType.class); in.skipLine(); return result;}, "[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.", List.of(options)), EventValidator::validateType);
    }

  public EventUpdater(EnumInputDevice in, OutputDevice out) {
    super(out);
    init(in);
  }

  public EventUpdater(EnumInputDevice in, OutputDevice out, List<Interactor<?>> interactors, List<TypedBiConsumer<Event,?>> setters, List<Object> objects, List<Supplier<?>> methods, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders, List<Function<Event,?>> getters, List<InteractiveUpdater<?>> updaters) {
    super(out, interactors, setters, objects, methods, validators, builders, getters, updaters);
    init(in);
  }
}