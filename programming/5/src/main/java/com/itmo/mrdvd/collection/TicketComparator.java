package com.itmo.mrdvd.collection;

import java.util.Comparator;

import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class TicketComparator implements Comparator<Ticket> {
  private final TicketField field;
  private final boolean descending;

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
        break;
      case CREATION_DATE:
        result = t1.getCreationDate().compareTo(t2.getCreationDate());
        break;
      case PRICE:
        result = Integer.compare(t1.getPrice(), t2.getPrice());
        break;
      case TYPE:
        result = t1.getType().compareTo(t2.getType());
        break;
      case EVENT:
        result = t1.getEvent().compareTo(t2.getEvent());
        break;
      case ID:
      default:
        result = t1.getId().compareTo(t2.getId());
    }
    return this.descending ? -result : result;
  }
}
