package com.itmo.mrdvd.validators;

import com.itmo.mrdvd.Node;
import com.itmo.mrdvd.Ticket;

public class NodeValidator extends ObjectValidator<Node> {
  private void init(ObjectValidator<Ticket> ticketValidator) {
    check(t -> t.getItem().getTicket(), ticketValidator);
  }

  public NodeValidator(ObjectValidator<Ticket> ticketValidator) {
    super();
    init(ticketValidator);
  }
}
