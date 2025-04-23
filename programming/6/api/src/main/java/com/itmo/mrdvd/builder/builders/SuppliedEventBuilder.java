package com.itmo.mrdvd.builder.builders;

import com.itmo.mrdvd.builder.validators.EventValidator;
import com.itmo.mrdvd.object.Event;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SuppliedEventBuilder extends SuppliedObjectBuilder<Event> {
  public SuppliedEventBuilder() {
    this(
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new HashSet<>());
  }

  public SuppliedEventBuilder(
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<SuppliedBuilder<?>> builders,
      Set<SuppliedBuilder<?>> hashedBuilders) {
    super(setters, methods, validators, builders, hashedBuilders);
    of(Event::new);
    setFromSupplies(Event::setName, EventValidator::validateName);
    setFromSupplies(Event::setDescription, EventValidator::validateDescription);
    setFromSupplies(Event::setType, EventValidator::validateType);
  }
}
