package com.itmo.mrdvd.collection;

import java.util.ArrayList;

import com.itmo.mrdvd.object.Ticket;

public class TicketCollection implements CollectionWorker<Ticket> {
   private ArrayList<Ticket> tickets;
   private IdGenerator ticketGenerator;
   private IdGenerator eventGenerator;
   public TicketCollection(IdGenerator ticketGen, IdGenerator eventGen) {
      tickets = new ArrayList<Ticket>();
      this.ticketGenerator = ticketGen;
      this.eventGenerator = eventGen;
   }
   //  0: success
   // -1: not valid obj
   public int addRaw(Ticket obj) {
      if (obj.isValid()) {
         if (getTicketIdGenerator().isTaken(obj.getId()) || getEventIdGenerator().isTaken(obj.getEvent().getId())) {
            return -1;
         }
         tickets.add(obj);
         getTicketIdGenerator().takeId(obj.getId());
         getEventIdGenerator().takeId(obj.getEvent().getId());
         return 0;
      }
      return -1;
   }
   //  0: success
   // -1: not valid obj
   @Override
   public int add(Ticket obj) {
      Long ticketId = getTicketIdGenerator().getId(obj);
      Long eventId = getEventIdGenerator().getId(obj.getEvent());
      obj.setId(ticketId);
      obj.getEvent().setId(eventId);
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
   @Override
   public void clear() {
      tickets.clear();
   }
}
