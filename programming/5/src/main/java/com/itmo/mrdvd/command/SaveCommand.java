package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.device.Serializer;

public class SaveCommand implements Command  {
   private final TicketCollection collection;
   private final Serializer<TicketCollection> serial;
   private final OutputDevice file, log;
   public SaveCommand(TicketCollection collect, Serializer<TicketCollection> serial, OutputDevice file, OutputDevice log) {
      this.collection = collect;
      this.serial = serial;
      this.file = file;
      this.log = log;
   }
   @Override
   public void execute(String[] params) {
      String result = serial.serialize(collection);
      if (result == null) {
         log.writeln("[ERROR] Ошибка сериализации коллекции.");
         return;
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
