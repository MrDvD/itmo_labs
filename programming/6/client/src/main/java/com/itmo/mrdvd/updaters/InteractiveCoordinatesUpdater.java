package com.itmo.mrdvd.updaters;

import com.itmo.mrdvd.builder.interactors.Interactor;
import com.itmo.mrdvd.builder.interactors.UserInteractor;
import com.itmo.mrdvd.builder.updaters.InteractiveObjectUpdater;
import com.itmo.mrdvd.builder.updaters.InteractiveUpdater;
import com.itmo.mrdvd.device.input.DataInputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.service.shell.AbstractShell;
import com.itmo.mrdvd.validators.CoordinatesValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InteractiveCoordinatesUpdater extends InteractiveObjectUpdater<Coordinates> {
  public InteractiveCoordinatesUpdater(AbstractShell shell) {
    this(
        shell,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public InteractiveCoordinatesUpdater(
      AbstractShell shell,
      List<Interactor<?>> interactors,
      List<BiConsumer> setters,
      List<Function<Coordinates, ?>> getters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<InteractiveUpdater> updaters) {
    super(interactors, setters, getters, methods, validators, updaters);
    addInteractiveChange(
        Coordinates::setX,
        Coordinates::getX,
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
    addInteractiveChange(
        Coordinates::setY,
        Coordinates::getY,
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
