package com.itmo.mrdvd.builder.updaters;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.UserInteractor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.validators.CoordinatesValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.object.Coordinates;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveCoordinatesUpdater
    extends InteractiveObjectUpdater<Coordinates, FloatInputDevice> {

  private void init() {
    addInteractiveChange(
        Coordinates::setX,
        Coordinates::getX,
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
    addInteractiveChange(
        Coordinates::setY,
        Coordinates::getY,
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
  }

  public InteractiveCoordinatesUpdater(FloatInputDevice in, OutputDevice out) {
    super(in, out);
    init();
  }

  public InteractiveCoordinatesUpdater(
      FloatInputDevice in,
      OutputDevice out,
      List<Interactor<?, FloatInputDevice>> interactors,
      List<TypedBiConsumer<Coordinates, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<Function<Coordinates, ?>> getters,
      List<InteractiveUpdater> updaters) {
    super(in, out, interactors, setters, objects, methods, validators, getters, updaters);
    init();
  }

  @Override
  public InteractiveCoordinatesUpdater setIn(FloatInputDevice in) {
    return new InteractiveCoordinatesUpdater(in, out);
  }
}
