package com.itmo.mrdvd.command;

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
import com.itmo.mrdvd.object.Ticket.TicketValidator;

public class UpdateCommand implements Command {
   private TicketCollection collect;
   private InputDevice in;
   private OutputDevice out;
   public UpdateCommand(TicketCollection collect, InputDevice in, OutputDevice out) {
      this.collect = collect;
      this.in = in;
      this.out = out;
   }
   public int validateParams(String[] params) {
      if (params.length != 1) {
         return -3;
      }
      Long id = TicketParser.parseId(params[0]);
      if (!TicketValidator.validateId(id)) {
         return -1;
      }
      if (!collect.getTicketIdGenerator().isTaken(id)) {
         return -2;
      }
      return 0;
   }
   @Override
   public void execute(String[] params) {
      int validationResult = validateParams(params);
      if (validationResult != 0) {
         switch (validationResult) {
            case -1:
               out.writeln("[ERROR] Неправильный формат ввода: id должен быть целым числом.");
               return;
            case -2:
               out.writeln("[ERROR] Указанный id не найден в коллекции.");
               return;
            default:
               out.writeln("[ERROR] Неправильный формат ввода параметров команды.");
               return;
         }
      }
      Long id = TicketParser.parseId(params[0]);
      Ticket ticket = collect.get(id);
      String name = in.read(String.format("Введите название билета [%s] > ", ticket.getName()));
      while (!name.isEmpty() && ticket.setName(name) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: название не должно быть пустым.");
         name = in.read(String.format("Введите название билета [%s] > ", ticket.getName()));
      }
      Coordinates coords = new Coordinates();
      String xString = in.read(String.format("Введите координату X [%.2f] > ", ticket.getCoordinates().getX()));
      Float x = CoordinatesParser.parseX(xString);
      while (!xString.isEmpty() && coords.setX(x) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
         xString = in.read(String.format("Введите координату X [%.2f] > ", ticket.getCoordinates().getX()));
         x = CoordinatesParser.parseX(xString);
      }
      String yString = in.read(String.format("Введите координату Y [%.2f] > ", ticket.getCoordinates().getY()));
      Float y = CoordinatesParser.parseY(yString);
      while (!yString.isEmpty() && coords.setY(y) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
         yString = in.read(String.format("Введите координату Y [%.2f] > ", ticket.getCoordinates().getY()));
         y = CoordinatesParser.parseY(yString);
      }
      ticket.setCoordinates(coords);
      String priceString = in.read(String.format("Введите стоимость билета (в у.е.) [%d] > ", ticket.getPrice()));
      int price = TicketParser.parsePrice(priceString);
      while (!priceString.isEmpty() && ticket.setPrice(price) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: введите натуральное число.");
         priceString = in.read(String.format("Введите стоимость билета (в у.е.) [%d] > ", ticket.getPrice()));
         price = TicketParser.parsePrice(priceString);
      }
      String typeMessage = "Доступные типы билета:\n";
      for (TicketType type : TicketType.values()) {
         typeMessage += "* " + type.name() + "\n";
      }
      typeMessage += String.format("Введите тип билета [%s] > ", ticket.getType().name());
      String ticketTypeString = in.read(typeMessage);
      TicketType ticketType = TicketParser.parseType(ticketTypeString);
      while (!ticketTypeString.isEmpty() && ticket.setType(ticketType) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: указанный тип билета не найден.");
         ticketTypeString = in.read(typeMessage);
         ticketType = TicketParser.parseType(ticketTypeString);
      }
      Event event = ticket.getEvent();
      String eventName = in.read(String.format("Введите название мероприятия [%s] > ", event.getName()));
      while (!eventName.isEmpty() && event.setName(eventName) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: название не должно быть пустым.");
         eventName = in.read(String.format("Введите название мероприятия [%s] > ", event.getName()));
      }
      String eventDesc = in.read(String.format("Введите небольшое описание мероприятия [%s] > ", event.getDescription()));
      while (!eventDesc.isEmpty() && event.setName(eventDesc) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: описание не должно быть пустым и превышать длину в 1190 символов.");
         eventDesc = in.read("Введите небольшое описание мероприятия > ");
      }
      String eventMessage = "Доступные виды мероприятия:\n";
      for (EventType type : EventType.values()) {
         eventMessage += "* " + type.name() + "\n";
      }
      eventMessage += String.format("Введите вид мероприятия [%s] > ", event.getEventType().name());
      String eventTypeString = in.read(eventMessage);
      EventType eventType = EventParser.parseType(eventTypeString);
      while (!eventTypeString.isEmpty() && event.setEventType(eventType) != 0) {
         out.writeln("[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.");
         eventTypeString = in.read(eventMessage);
         eventType = EventParser.parseType(eventTypeString);
      }
      ticket.setEvent(event, false);
      collect.update(id, ticket);
      out.writeln("[INFO] Билет успешно обновлён в коллекции.");
   }
   @Override
   public String name() {
      return "update";
   }
   @Override
   public String signature() {
      return name() + " id {element}";
   }
   @Override
   public String description() {
      return "обновить значение элемента коллекции, id которого равен заданному";
   }
}
