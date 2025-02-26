package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;

public class InfoCommand implements Command  {
   private TicketCollection collection;
   private OutputDevice out;
   public InfoCommand(TicketCollection collect, OutputDevice out) {
      this.collection = collect;
      this.out = out;
   }
   @Override
   public void execute(String[] params) {
      
   }
   @Override
   public String name() {
      return "info";
   }
   @Override
   public String signature() {
      return name();
   }
   @Override
   public String description() {
      return "вывести в стандартный поток вывода информацию о коллекции";
   }
}
