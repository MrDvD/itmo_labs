package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.Deserializer;
import com.itmo.mrdvd.device.InputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Ticket;

public class LoadCommand implements Command {
   private final InputDevice in;
   private final TicketCollection collection;
   private final Deserializer<TicketCollection> deserial;
   private final OutputDevice out;
   public LoadCommand(InputDevice in, TicketCollection collection, Deserializer<TicketCollection> deserial, OutputDevice out) {
      this.in = in;
      this.collection = collection;
      this.deserial = deserial;
      this.out = out;
   }
   @Override
   public void execute(String[] params) {
      int code = in.openIn();
      if (code != 0) {
         switch (code) {
            case -1 -> out.writeln("[WARN] Файла с коллекцией не существует, считывать нечего.");
            case -3 -> out.writeln("[ERROR] Не указан путь к файлу с коллекцией.");
            default -> out.writeln("[ERROR] Ошибка доступа к файлу с коллекцией.");
         }
         return;
      }
      String fileContent = in.read();
      in.closeIn();
      if (fileContent == null) {
         out.writeln("[ERROR] Ошибка чтения файла с коллекцией.");
         return;
      }
      TicketCollection loaded = deserial.deserialize(fileContent);
      if (loaded != null) {
         collection.clear();
         for (Ticket t : loaded) {
            collection.addRaw(t);
         }
         out.writeln("[INFO] Коллекция успешно считана из файла.");
      } else {
         out.writeln("[ERROR] Невозможно конвертировать структуру файла с коллекцией.");
      }
   }
   @Override
   public String name() {
      return "load";
   }
   @Override
   public String signature() {
      return name();
   }
   @Override
   public String description() {
      return "загрузить коллекцию из файла";
   }
}
