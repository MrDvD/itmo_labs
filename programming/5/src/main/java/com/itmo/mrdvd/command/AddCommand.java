package com.itmo.mrdvd.command;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.InputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.EventType;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;

public class AddCommand implements Command {
   private TicketCollection collect;
   private InputDevice in;
   private OutputDevice out;
   public AddCommand(TicketCollection collect, InputDevice in, OutputDevice out) {
      this.collect = collect;
      this.in = in;
      this.out = out;
   }
   @Override
   public void execute(String[] params) {
      Ticket ticket = new Ticket();
      String name = in.read("Введите название билета: ");
      while (ticket.setName(name) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: название не должно быть пустым.");
         name = in.read("Введите название билета: ");
      }
      Coordinates coords = new Coordinates();
      String strX = in.read("Введите координату X: ");
      while (coords.setX(strX) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
         strX = in.read("Введите координату X: ");
      }
      String strY = in.read("Введите координату Y: ");
      while (coords.setY(strY) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
         strY = in.read("Введите координату Y: ");
      }
      ticket.setCoordinates(coords);
      String strPrice = in.read("Введите стоимость билета (в у.е.): ");
      while (ticket.setPrice(strPrice) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите натуральное число.");
         strPrice = in.read("Введите стоимость билета (в у.е.): ");
      }
      String typeMessage = "Доступные типы билета:\n";
      for (TicketType type : TicketType.values()) {
         typeMessage += "* " + type.name() + "\n";
      }
      typeMessage += "Введите тип билета: ";
      String strType = in.read(typeMessage);
      while (ticket.setType(strType) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: указанный тип билета не найден.");
         strType = in.read(typeMessage);
      }
      String eventMessage = "Доступные виды мероприятия:\n";
      for (EventType type : EventType.values()) {
         eventMessage += "* " + type.name() + "\n";
      }
      eventMessage += "Введите вид мероприятия: ";
      String strEvent = in.read(eventMessage);
      while (ticket.setEvent(strEvent) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.");
         strEvent = in.read(eventMessage);
      }
      collect.add(ticket);
      out.writeln("Билет успешно добавлен в коллекцию.");
   }
   @Override
   public String name() {
      return "add {collection}";
   }
   @Override
   public String description() {
      return "добавить новый элемент в коллекцию";
   }
}
