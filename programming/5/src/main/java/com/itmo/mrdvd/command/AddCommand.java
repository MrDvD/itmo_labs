package com.itmo.mrdvd.command;

import java.time.LocalDateTime;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.InputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.EventType;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.TicketType;
import com.itmo.mrdvd.object.Coordinates.CoordinatesParser;
import com.itmo.mrdvd.object.Event.EventParser;
import com.itmo.mrdvd.object.Ticket.TicketParser;

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
      ticket.setCreationDate(LocalDateTime.now());
      String name = in.read("Введите название билета > ");
      while (ticket.setName(name) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: название не должно быть пустым.");
         name = in.read("Введите название билета > ");
      }
      Coordinates coords = new Coordinates();
      Float x = CoordinatesParser.parseX(in.read("Введите координату X > "));
      while (coords.setX(x) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
         x = CoordinatesParser.parseX(in.read("Введите координату X > "));
      }
      Float y = CoordinatesParser.parseY(in.read("Введите координату Y > "));
      while (coords.setY(y) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
         y = CoordinatesParser.parseY(in.read("Введите координату Y > "));
      }
      ticket.setCoordinates(coords);
      int price = TicketParser.parsePrice(in.read("Введите стоимость билета (в у.е.) > "));
      while (ticket.setPrice(price) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите натуральное число.");
         price = TicketParser.parsePrice(in.read("Введите стоимость билета (в у.е.) > "));
      }
      String typeMessage = "Доступные типы билета:\n";
      for (TicketType type : TicketType.values()) {
         typeMessage += "* " + type.name() + "\n";
      }
      typeMessage += "Введите тип билета > ";
      TicketType ticketType = TicketParser.parseType(in.read(typeMessage));
      while (ticket.setType(ticketType) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: указанный тип билета не найден.");
         ticketType = TicketParser.parseType(in.read(typeMessage));
      }
      Event event = new Event();
      String eventName = in.read("Введите название мероприятия > ");
      while (event.setName(eventName) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: название не должно быть пустым.");
         eventName = in.read("Введите название мероприятия > ");
      }
      String eventDesc = in.read("Введите небольшое описание мероприятия > ");
      while (event.setName(eventDesc) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: описание не должно быть пустым и превышать длину в 1190 символов.");
         eventDesc = in.read("Введите небольшое описание мероприятия > ");
      }
      String eventMessage = "Доступные виды мероприятия:\n";
      for (EventType type : EventType.values()) {
         eventMessage += "* " + type.name() + "\n";
      }
      eventMessage += "Введите вид мероприятия > ";
      EventType eventType = EventParser.parseType(in.read(eventMessage));
      while (event.setEventType(eventType) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.");
         eventType = EventParser.parseType(in.read(eventMessage));
      }
      ticket.setEvent(event);
      collect.add(ticket);
      out.writeln("[INFO] Билет успешно добавлен в коллекцию.");
   }
   @Override
   public String name() {
      return "add";
   }
   @Override
   public String signature() {
      return name() + " {params}";
   }
   @Override
   public String description() {
      return "добавить новый элемент в коллекцию";
   }
}
