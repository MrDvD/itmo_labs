package com.itmo.mrdvd.builders;

import com.itmo.mrdvd.builder.builders.SuppliedBuilder;
import com.itmo.mrdvd.builder.builders.SuppliedObjectBuilder;
import com.itmo.mrdvd.builder.validators.CoordinatesValidator;
import com.itmo.mrdvd.object.Coordinates;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SuppliedCoordinatesBuilder extends SuppliedObjectBuilder<Coordinates> {
  public SuppliedCoordinatesBuilder() {
    this(
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new HashSet<>());
  }

  public SuppliedCoordinatesBuilder(
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<SuppliedBuilder<?>> builders,
      Set<SuppliedBuilder<?>> hashedBuilders) {
    super(setters, methods, validators, builders, hashedBuilders);
    of(Coordinates::new);
    setFromSupplies(Coordinates::setX, CoordinatesValidator::validateX);
    setFromSupplies(Coordinates::setY, CoordinatesValidator::validateY);
  }
}
