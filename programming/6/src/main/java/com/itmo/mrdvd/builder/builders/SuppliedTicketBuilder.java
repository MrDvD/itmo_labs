package com.itmo.mrdvd.builder.builders;

import com.itmo.mrdvd.builder.validators.TicketValidator;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SuppliedTicketBuilder extends SuppliedObjectBuilder<Ticket> {
  protected final SuppliedBuilder<Event> eventBuilder;
  protected final SuppliedBuilder<Coordinates> coordsBuilder;

  public SuppliedTicketBuilder(
      SuppliedBuilder<Event> eventBuilder, SuppliedBuilder<Coordinates> coordsBuilder) {
    this(
        eventBuilder,
        coordsBuilder,
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new ArrayList<>(),
        new HashSet<>());
  }

  public SuppliedTicketBuilder(
      SuppliedBuilder<Event> eventBuilder,
      SuppliedBuilder<Coordinates> coordsBuilder,
      List<BiConsumer> setters,
      List<Supplier<?>> methods,
      List<Predicate> validators,
      List<SuppliedBuilder<?>> builders,
      Set<SuppliedBuilder<?>> hashedBuilders) {
    super(setters, methods, validators, builders, hashedBuilders);
    this.eventBuilder = eventBuilder;
    this.coordsBuilder = coordsBuilder;
    of(Ticket::new);
    setFromSupplies(Ticket::setName, TicketValidator::validateName);
    addSuppliedBuilder(Ticket::setCoordinates, this.coordsBuilder);
    setFromSupplies(Ticket::setPrice, TicketValidator::validatePrice);
    setFromSupplies(Ticket::setType, TicketValidator::validateType);
    addSuppliedBuilder(Ticket::setEvent, this.eventBuilder);
    set(Ticket::setCreationDate, LocalDateTime::now, TicketValidator::validateCreationDate);
  }
}
