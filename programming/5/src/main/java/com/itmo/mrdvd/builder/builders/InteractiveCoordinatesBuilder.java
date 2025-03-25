package com.itmo.mrdvd.builder.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.itmo.mrdvd.builder.Interactor;
import com.itmo.mrdvd.builder.UserInteractor;
import com.itmo.mrdvd.builder.functionals.TypedBiConsumer;
import com.itmo.mrdvd.builder.functionals.TypedPredicate;
import com.itmo.mrdvd.builder.validators.CoordinatesValidator;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.input.FloatInputDevice;
import com.itmo.mrdvd.object.Coordinates;

public class InteractiveCoordinatesBuilder extends InteractiveObjectBuilder<Coordinates> {
  private void init(Supplier<FloatInputDevice> in) {
    of(Coordinates::new);
    addInteractiveSetter(
        Coordinates::setX,
        Float.class,
        new UserInteractor<>(
            "X-координата",
            in,
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
            in,
            (FloatInputDevice x) -> {
              Optional<Float> res = x.readFloat();
              x.skipLine();
              return res;
            },
            "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).",
            "разделитель - точка"),
        CoordinatesValidator::validateY);
  }

  public InteractiveCoordinatesBuilder(Supplier<FloatInputDevice> in, OutputDevice out) {
    super(out);
    init(in);
  }

  public InteractiveCoordinatesBuilder(
      Supplier<FloatInputDevice> in,
      OutputDevice out,
      List<Interactor<?>> interactors,
      List<TypedBiConsumer<Coordinates, ?>> setters,
      List<Object> objects,
      List<Supplier<?>> methods,
      List<TypedPredicate<?>> validators,
      List<InteractiveBuilder<?>> builders) {
    super(out, interactors, setters, objects, methods, validators, builders);
    init(in);
  }
}
