package com.itmo.mrdvd.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.itmo.mrdvd.object.Ticket;

public class TicketCollection implements CollectionWorker<Ticket> {
   private ArrayList<Ticket> tickets;
   private IdGenerator generator;
   public TicketCollection() {
      tickets = new ArrayList<Ticket>();
      generator = new TicketIdGenerator();
   }
   public class TicketIdGenerator implements IdGenerator {
      Set<Long> usedIds;
      public TicketIdGenerator() {
         this.usedIds = new HashSet<Long>();
      }
      @Override
      public boolean isTaken(Long id) {
         return usedIds.contains(id);
      }
      @Override
      public Long getId(Object obj) {
         if (obj == null) {
            return null;
         }
         Long newId = Math.abs(Long.valueOf(obj.hashCode()));
         while (isTaken(newId) || newId == 0) {
            newId = Math.abs(newId + Math.round(Math.random() * 100000000000L - 50000000000L));
         }
         usedIds.add(newId);
         return newId;
      }
      //  0: success
      // -1: id does not exist
      // -2: incorrect id value
      @Override
      public int freeId(Long id) {
         if (id == null || id <= 0) {
            return -2;
         }
         if (!usedIds.contains(id)) {
            return -1;
         }
         usedIds.remove(id);
         return 0;
      }
   }
   //  0: success
   // -1: not valid obj
   public int addRaw(Ticket obj) {
      if (obj.isValid()) {
         tickets.add(obj);
         return 0;
      }
      return -1;
   }
   //  0: success
   // -1: not valid obj
   @Override
   public int add(Ticket obj) {
      Long id = getIdGenerator().getId(obj);
      obj.setId(id);
      int returnCode = addRaw(obj);
      if (returnCode != 0) {
         getIdGenerator().freeId(id);
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
   public IdGenerator getIdGenerator() {
      return generator;
   }
   @Override
   public void clear() {
      tickets.clear();
   }
}
