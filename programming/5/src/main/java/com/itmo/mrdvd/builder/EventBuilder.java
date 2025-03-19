package com.itmo.mrdvd.builder;

import java.util.List;
import java.util.Optional;

import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;

public class EventBuilder extends InteractiveBuilder<Event> {
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

  private void initSetters(EnumInputDevice in) {
      addInteractiveSetter(Event::setName, String.class, new UserInteractor<String>("Имя мероприятия", in::read, "[ERROR] Неправильный формат ввода: имя не должно быть пустым."), EventValidator::validateName);
      addInteractiveSetter(Event::setDescription, String.class, new UserInteractor<String>("Описание мероприятия", in::read, "[ERROR] Неправильный формат ввода: описание не должно быть пустым и превышать длину в 1190 символов."), EventValidator::validateDescription);
      String[] options = new String[EventType.values().length];
      for (int i = 0; i < EventType.values().length; i++) {
        options[i] = EventType.values()[i].toString();
      }
      addInteractiveSetter(Event::setType, EventType.class, new UserInteractor<Enum<EventType>>("Тип мероприятия", () -> { Optional<Enum<EventType>> result = in.readEnum(EventType.class); in.skipLine(); return result;}, "[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.", List.of(options)), EventValidator::validateType);
    }

  public EventBuilder(EnumInputDevice in, OutputDevice out) {
    super(new Event(), out);
    initSetters(in);
  }

  public EventBuilder(EnumInputDevice in, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<Event,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators, List<InteractiveBuilder<?>> builders) {
    super(new Event(), out, interactors, setters, objects, validators, builders);
    initSetters(in);
  }
}
