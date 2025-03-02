package com.itmo.mrdvd.collection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketField;

public class TicketCollection implements CollectionWorker<Ticket>, Iterable<Ticket> {
  @JsonProperty private ArrayList<Ticket> tickets;
  private final IdGenerator ticketGenerator;
  private final IdGenerator eventGenerator;
  private TicketCollectionMetadata meta;

  static public class TicketCollectionMetadata {
    @JsonProperty private LocalDateTime creationTime;
    @JsonProperty private String type;
    private String name;

    public TicketCollectionMetadata() {}

    public TicketCollectionMetadata(String name) {
      this.creationTime = LocalDateTime.now();
      this.type = Ticket.class.getSimpleName();
      this.name = name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public LocalDateTime getCreationTime() {
      return this.creationTime;
    }

    public String getName() {
      return this.name;
    }

   //  public int getCount() {
   //    return tickets.size();
   //  }

    public String getType() {
      return this.type;
    }

    @Override
    public String toString() {
      String result = "";
      result += "# # # Метаданные коллекции # # #\n";
      result += String.format("НАЗВАНИЕ: %s\n", getName());
      result += String.format("ТИП: %s\n", getType());
      result +=
          String.format(
              "ДАТА ИНИЦИАЛИЗАЦИИ: %s\n",
              getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      // result += String.format("КОЛИЧЕСТВО ЭЛЕМЕНТОВ: %d", getCount());
      return result;
    }
  }

  public TicketCollection() {
    this(null, null, null);
  }

  public TicketCollection(String name, IdGenerator ticketGen, IdGenerator eventGen) {
    this.tickets = new ArrayList<>();
    this.meta = new TicketCollectionMetadata(name);
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

  public TicketCollectionMetadata getMetadata() {
    return this.meta;
  }

  public void setMetadata(TicketCollectionMetadata meta) {
    this.meta = meta;
  }

  @Override
  public void clear() {
    tickets.clear();
  }
}
