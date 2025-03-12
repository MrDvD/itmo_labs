package com.itmo.mrdvd.command;

import java.time.LocalDateTime;
import java.util.Optional;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.InteractiveInputDevice;
import com.itmo.mrdvd.device.OutputDevice;
import com.itmo.mrdvd.object.Coordinates;
import com.itmo.mrdvd.object.Coordinates.CoordinatesParser;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Event.EventParser;
import com.itmo.mrdvd.object.EventType;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.object.Ticket.TicketParser;
import com.itmo.mrdvd.object.TicketType;

public class AddCommand implements Command {
  protected final TicketCollection collect;
  protected final InteractiveInputDevice in;
  protected final OutputDevice out;

  public AddCommand(TicketCollection collect, InteractiveInputDevice in, OutputDevice out) {
    this.collect = collect;
    this.in = in;
    this.out = out;
  }

  protected Ticket createTicketInteractive() {
   Ticket ticket = new Ticket();
    ticket.setCreationDate(LocalDateTime.now());
    Optional<String> name = in.read("Введите название билета > ");
    while (name.isPresent() && ticket.setName(name.get()) != 0) {
      out.writeln("[ERROR] Неправильный формат ввода: название не должно быть пустым.");
      name = in.read("Введите название билета > ");
    }
    Coordinates coords = new Coordinates();
    Optional<String> x = in.read("Введите координату X > ");
    while (x.isPresent() && coords.setX(CoordinatesParser.parseX(x.get())) != 0) {
      out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
      x = in.read("Введите координату X > ");
    }
    Optional<String> y = in.read("Введите координату Y > ");
    while (y.isPresent() && coords.setY(CoordinatesParser.parseY(y.get())) != 0) {
      out.writeln("[ERROR] Неправильный формат ввода: введите число (возможно, дробное).");
      y = in.read("Введите координату Y > ");
    }
    ticket.setCoordinates(coords);
    Optional<String> price = in.read("Введите стоимость билета (в у.е.) > ");
    while (price.isPresent() && ticket.setPrice(TicketParser.parsePrice(price.get())) != 0) {
      out.writeln("[ERROR] Неправильный формат ввода: введите натуральное число.");
      price = in.read("Введите стоимость билета (в у.е.) > ");
    }
    String typeMessage = "Доступные типы билета:\n";
    for (TicketType type : TicketType.values()) {
      typeMessage += "* " + type.name() + "\n";
    }
    typeMessage += "Введите тип билета > ";
    Optional<String> ticketType = in.read(typeMessage);
    while (ticketType.isPresent() && ticket.setType(TicketParser.parseType(ticketType.get())) != 0) {
      out.writeln("[ERROR] Неправильный формат ввода: указанный тип билета не найден.");
      ticketType = in.read(typeMessage);
    }
    Event event = new Event();
    event.setId(collect.getEventIdGenerator().bookId(event));
    Optional<String> eventName = in.read("Введите название мероприятия > ");
    while (event.setName(eventName.get()) != 0) {
      out.writeln("[ERROR] Неправильный формат ввода: название не должно быть пустым.");
      eventName = in.read("Введите название мероприятия > ");
    }
    Optional<String> eventDesc = in.read("Введите небольшое описание мероприятия > ");
    while (event.setDescription(eventDesc.get()) != 0) {
      out.writeln(
          "[ERROR] Неправильный формат ввода: описание не должно быть пустым и превышать длину в 1190 символов.");
      eventDesc = in.read("Введите небольшое описание мероприятия > ");
    }
    String eventMessage = "Доступные виды мероприятия:\n";
    for (EventType type : EventType.values()) {
      eventMessage += "* " + type.name() + "\n";
    }
    eventMessage += "Введите вид мероприятия > ";
    Optional<String> eventType = in.read(eventMessage);
    while (eventType.isPresent() && event.setEventType(EventParser.parseType(eventType.get())) != 0) {
      out.writeln("[ERROR] Неправильный формат ввода: указанный вид мероприятия не найден.");
      eventType = in.read(eventMessage);
    }
    ticket.setEvent(event);
    return ticket;
  }

  @Override
  public void execute(String[] params) {
    Optional<Ticket> result = collect.add(createTicketInteractive());
    if (result.isPresent()) {
      out.writeln("[INFO] Билет успешно добавлен в коллекцию.");
    } else {
      out.writeln("[ERROR] Не удалось добавить билет в коллекцию.");
    }
  }

  @Override
  public String name() {
    return "add";
  }

  @Override
  public String signature() {
    return name() + " {element}";
  }

  @Override
  public String description() {
    return "добавить новый элемент в коллекцию";
  }
}
