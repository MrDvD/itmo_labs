package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;

public class ShowCommand implements Command  {
   private TicketCollection collection;
   private OutputDevice out;
   public ShowCommand(TicketCollection collect, OutputDevice out) {
      this.collection = collect;
      this.out = out;
   }
   @Override
   public void execute(String[] params) {
      for (Ticket ticket : collection) {
         out.writeln(ticket.toString());
      }
      out.writeln("[INFO] Конец коллекции.");
   }
   @Override
   public String name() {
      return "show";
   }

   @Override
   public String signature() {
      return name();
   }
   @Override
   public String description() {
      return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
   }
}
