package com.itmo.mrdvd.collection.ticket;

import com.itmo.mrdvd.Ticket;
import com.itmo.mrdvd.object.TicketField;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;

public class TicketComparator implements Comparator<Ticket> {
  private final TicketField field;
  private final boolean descending;

  public TicketComparator(TicketField field) {
    this(field, false);
  }

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
        LocalDateTime d1 =
            Instant.ofEpochSecond(t1.getCreateDate().getSeconds(), t1.getCreateDate().getNanos())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime d2 =
            Instant.ofEpochSecond(t2.getCreateDate().getSeconds(), t2.getCreateDate().getNanos())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        result = d1.compareTo(d2);
        break;
      case PRICE:
        result = Integer.compare(t1.getPrice(), t2.getPrice());
        break;
      case TYPE:
        result = t1.getType().compareTo(t2.getType());
        break;
      case EVENT:
        result = Long.valueOf(t1.getEvent().getId()).compareTo(t2.getEvent().getId());
        break;
      case ID:
      default:
        result = Long.valueOf(t1.getId()).compareTo(t2.getId());
    }
    return this.descending ? -result : result;
  }
}
