package com.itmo.mrdvd.builders;

import com.itmo.mrdvd.builder.builders.InteractiveBuilder;
import com.itmo.mrdvd.builder.builders.InteractiveObjectBuilder;
import com.itmo.mrdvd.builder.interactors.Interactor;
import com.itmo.mrdvd.builder.interactors.UserInteractor;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.service.shell.AbstractShell;
import com.itmo.mrdvd.validators.CoordinatesValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveCoordinatesBuilder extends InteractiveObjectBuilder<Coordinates> {
  public InteractiveCoordinatesBuilder(AbstractShell shell) {
    this(
        shell,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveCoordinatesBuilder(
      AbstractShell shell,
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveBuilder<?>> builders) {
    super(interactors, setters, methods, validators, builders);
    of(Coordinates::new);
    addInteractiveSetter(
        Coordinates::setX,
        new UserInteractor<>(
            "X-координата",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<Float> res = x.readFloat();
              x.skipLine();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).\n",
            "разделитель - точка"),
        CoordinatesValidator::validateX);
    addInteractiveSetter(
        Coordinates::setY,
        new UserInteractor<>(
            "Y-координата",
            () -> {
              DataInputDevice x = shell.getTty().get().getIn();
              Optional<Float> res = x.readFloat();
              x.skipLine();
              return res;
            },
            (String msg) -> {
              shell.getTty().get().getOut().write(msg);
            },
            "[ERROR] Неправильный формат ввода: введите число (возможно, дробное).\n",
            "разделитель - точка"),
        CoordinatesValidator::validateY);
  }
}
