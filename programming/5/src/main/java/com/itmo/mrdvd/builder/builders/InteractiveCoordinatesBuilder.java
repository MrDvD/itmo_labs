package com.itmo.mrdvd.builder.builders;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.UserInteractor;
import com.itmo.mrdvd.builder.validators.CoordinatesValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveCoordinatesBuilder
    extends InteractiveObjectBuilder<Coordinates, FloatInputDevice> {
  private InteractiveCoordinatesBuilder init() {
    of(Coordinates::new);
    addInteractiveSetter(
        Coordinates::setX,
        Float.class,
        new UserInteractor<>(
            "X-координата",
            (FloatInputDevice x) -> {
              Optional<Float> res = x.readFloat();
              x.skipLine();
              return res;
            },
            "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).",
            "разделитель - точка"),
        CoordinatesValidator::validateX);
    addInteractiveSetter(
        Coordinates::setY,
        Float.class,
        new UserInteractor<>(
            "Y-координата",
            (FloatInputDevice x) -> {
              Optional<Float> res = x.readFloat();
              x.skipLine();
              return res;
            },
            "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).",
            "разделитель - точка"),
        CoordinatesValidator::validateY);
    return this;
  }

  public InteractiveCoordinatesBuilder(FloatInputDevice in, OutputDevice out) {
    super(in, out);
    init();
  }

  public InteractiveCoordinatesBuilder(
      FloatInputDevice in,
      OutputDevice out,
      List<Interactor<?, FloatInputDevice>> interactors,
      List<BiConsumer> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveBuilder<?, ?>> builders) {
    super(in, out, interactors, setters, objects, methods, validators, builders);
    init();
  }

  @Override
  public InteractiveCoordinatesBuilder setIn(FloatInputDevice in) {
    return new InteractiveCoordinatesBuilder(in, out);
  }
}
