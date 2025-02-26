package com.itmo.mrdvd.collection;

import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;
import java.util.Comparator;

public class TicketComparator implements Comparator<Ticket> {
  private TicketField field;
  private boolean descending;

  public TicketComparator(TicketField field, boolean descending) {
    this.field = field;
    this.descending = descending;
  }

  @Override
  public int compare(Ticket t1, Ticket t2) {
    int result;
    switch (field) {
      case NAME:
        result = t1.getName().compareTo(t2.getName());
      case CREATION_DATE:
        result = t1.getCreationDate().compareTo(t2.getCreationDate());
      case PRICE:
        result = Integer.compare(t1.getPrice(), t2.getPrice());
      case TYPE:
        result = t1.getType().compareTo(t2.getType());
      case EVENT:
        result = t1.getEvent().compareTo(t2.getEvent());
      case ID:
      default:
        result = t1.getId().compareTo(t2.getId());
    }
    return this.descending ? -result : result;
  }
}
