package com.itmo.mrdvd.collection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class TicketCollection implements CollectionWorker<Ticket>, Iterable<Ticket> {
  private final ArrayList<Ticket> tickets;
  private final IdGenerator ticketGenerator;
  private final IdGenerator eventGenerator;
  private LocalDateTime updateTime;

  public TicketCollection(IdGenerator ticketGen, IdGenerator eventGen) {
    this.tickets = new ArrayList<>();
    this.ticketGenerator = ticketGen;
    this.eventGenerator = eventGen;
  }

  //  0: success
  // -1: not valid obj
  public int addRaw(Ticket obj) {
    if (obj.isValid()) {
      if (getTicketIdGenerator().isTaken(obj.getId())
          || getEventIdGenerator().isTaken(obj.getEvent().getId())) {
        return -1;
      }
      getTicketIdGenerator().takeId(obj.getId());
      getEventIdGenerator().takeId(obj.getEvent().getId());
      tickets.add(obj);
      return 0;
    }
    return -1;
  }

  //  0: success
  // -1: not valid obj
  @Override
  public int add(Ticket obj) {
    if (obj == null || obj.getEvent() == null) {
      return -1;
    }
    Long ticketId = null, eventId = null;
    if (obj.getId() == null) {
      ticketId = getTicketIdGenerator().bookId(obj);
      obj.setId(ticketId);
    }
    if (obj.getEvent().getId() == null) {
      eventId = getEventIdGenerator().bookId(obj.getEvent());
      obj.getEvent().setId(eventId);
    }
    int returnCode = addRaw(obj);
    if (returnCode != 0) {
      getTicketIdGenerator().freeId(ticketId);
      getEventIdGenerator().freeId(eventId);
    }
    return returnCode;
  }

  @Override
  public Ticket get(Long id) {
    for (Ticket ticket : tickets) {
      if (ticket.getId().equals(id)) {
        return ticket;
      }
    }
    return null;
  }

  @Override
  public Iterator<Ticket> iterator() {
    return tickets.iterator();
  }

  //  0: success
  // -1: not valid obj
  // -2: not existing id object
  @Override
  public int update(Long id, Ticket obj) {
    if (obj.isValid()) {
      for (int i = 0; i < tickets.size(); i++) {
        Ticket ticket = tickets.get(i);
        if (ticket.getId().equals(id)) {
          tickets.set(i, obj);
          return 0;
        }
      }
      return -2;
    } else {
      return -1;
    }
  }

  //  0: success
  // -2: not existing id object
  @Override
  public int remove(Long id) {
    for (int i = 0; i < tickets.size(); i++) {
      Ticket ticket = tickets.get(i);
      if (ticket.getId().equals(id)) {
        tickets.remove(i);
        return 0;
      }
    }
    return -2;
  }

  //  0: success
  // -2: not existing index
  public int removeAt(int index) {
    if (index >= 0 && index < tickets.size()) {
      tickets.set(index, null);
      return 0;
    }
    return -2;
  }

  public int removeLast() {
    return removeAt(tickets.size() - 1);
  }

  public IdGenerator getTicketIdGenerator() {
    return ticketGenerator;
  }

  public IdGenerator getEventIdGenerator() {
    return eventGenerator;
  }

  public ArrayList<Ticket> sort(TicketField field) {
    return sort(field, false);
  }

  public ArrayList<Ticket> sort(TicketField field, boolean descending) {
    ArrayList<Ticket> sortedList = new ArrayList<>(tickets);
    sortedList.sort(new TicketComparator(field, descending));
    return sortedList;
  }

  @Override
  public void clear() {
    tickets.clear();
  }
}
