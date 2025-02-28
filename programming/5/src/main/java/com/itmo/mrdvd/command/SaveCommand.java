package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;
import com.itmo.mrdvd.object.Ticket;

public class SaveCommand implements Command  {
   private final TicketCollection collection;
   private final Serializer<Ticket> serial;
   private final OutputDevice file, log;
   public SaveCommand(TicketCollection collect, Serializer<Ticket> serial, OutputDevice file, OutputDevice log) {
      this.collection = collect;
      this.serial = serial;
      this.file = file;
      this.log = log;
   }
   @Override
   public void execute(String[] params) {
      String result = "";
      for (Ticket t : collection) {
         String curr = serial.serialize(t);
         if (curr == null) {
            log.writeln(String.format("[ERROR] Ошибка сериализации билета # %d.", t.getId()));
            return;
         }
      }
      if (file.writeln(result) == 0) {
         log.writeln("[INFO] Коллекция успешно записана в файл.");
      } else {
         log.writeln("[ERROR] Произошла ошибка при записи в файл.");
      }
   }
   @Override
   public String name() {
      return "save";
   }
   @Override
   public String signature() {
      return name();
   }
   @Override
   public String description() {
      return "сохранить коллекцию в файл";
   }
}
