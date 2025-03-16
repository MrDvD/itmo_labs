package com.itmo.mrdvd.builder;

import java.util.List;

import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.EnumInputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
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

  private void initSetters(FloatInputDevice inFloat, EnumInputDevice inEnum) {
      addInteractiveSetter(Event::setName, String.class, new UserInteractor<String>("Имя мероприятия", inFloat::read), EventValidator::validateName);
      addInteractiveSetter(Event::setDescription, String.class, new UserInteractor<String>("Описание мероприятия", inFloat::read), EventValidator::validateDescription);
      String[] options = new String[EventType.values().length];
      for (int i = 0; i < EventType.values().length; i++) {
        options[i] = EventType.values()[i].toString();
      }
      addInteractiveSetter(Event::setType, EventType.class, new UserInteractor<Enum<EventType>>("Тип мероприятия", () -> inEnum.readEnum(EventType.class), List.of(options)), EventValidator::validateType);
    }

  public EventBuilder(Event rawObject, FloatInputDevice inFloat, EnumInputDevice inEnum, OutputDevice out) {
    super(rawObject, out);
    initSetters(inFloat, inEnum);
  }

  public EventBuilder(Event rawObject, FloatInputDevice inFloat, EnumInputDevice inEnum, OutputDevice out, List<UserInteractor<?>> interactors, List<TypedBiConsumer<Event,?>> setters, List<Object> objects, List<TypedPredicate<?>> validators) {
    super(rawObject, out, interactors, setters, objects, validators);
    initSetters(inFloat, inEnum);
  }
}
