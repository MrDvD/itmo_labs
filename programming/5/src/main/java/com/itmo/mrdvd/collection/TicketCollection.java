package com.itmo.mrdvd.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.itmo.mrdvd.object.Ticket;

public class TicketCollection implements CollectionWorker<Ticket> {
   private ArrayList<Ticket> tickets;
   private Map<Long, Integer> idToIndexTable;
   private Set<Integer> freeIndexes;
   public TicketCollection() {
      tickets = new ArrayList<Ticket>();
      idToIndexTable = new HashMap<Long, Integer>();
      freeIndexes = new HashSet<Integer>();
   }
   //  0: success
   // -1: not valid obj
   @Override
   public int add(Ticket obj) {
      if (obj.isValid()) {
         if (freeIndexes.isEmpty()) {
            idToIndexTable.put(obj.getId(), tickets.size());
            tickets.add(obj);
         } else {
            int idx = freeIndexes.iterator().next();
            freeIndexes.remove(idx);
            idToIndexTable.put(obj.getId(), idx);
         }
         return 0;
      }
      return -1;
   }
   //  0: success
   // -1: not valid obj
   // -2: not existing id object
   @Override
   public int update(Long id, Ticket obj) {
      if (id >= tickets.size() || !idToIndexTable.containsKey(id)) {
         return -2;
      }
      if (obj.isValid()) {
         tickets.set(idToIndexTable.get(obj.getId()), obj);
         return 0;
      } else {
         return -1;
      }
   }
   //  0: success
   // -2: not existing id object
   @Override
   public int remove(Long id) {
      if (!idToIndexTable.containsKey(id)) {
         return -2;
      }
      tickets.set(idToIndexTable.get(id), null);
      freeIndexes.add(idToIndexTable.get(id));
      idToIndexTable.remove(id);
      return 0;
   }
   @Override
   public void clear() {
      tickets.clear();
      idToIndexTable.clear();
      freeIndexes.clear();
   }
}
