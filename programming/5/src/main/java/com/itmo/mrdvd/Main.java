package com.itmo.mrdvd;

import java.util.ArrayList;
import java.util.List;

import com.itmo.mrdvd.builder.TicketBuilder;
import com.itmo.mrdvd.builder.TypedBiConsumer;
import com.itmo.mrdvd.builder.TypedConsumer;
import com.itmo.mrdvd.object.Event;
import com.itmo.mrdvd.object.Ticket;

/*
 * TODO:
 * 1. Add BuilderObject in AddCommand as passed param
 * 2. Raise Exceptions instead of returning an int.
 * 2.5. Refactor IdGenerator logic
 * 3. Update UpdateCommand (with Optionals)
 * 4. Where to put validation?
 * 5. Where to put parsing? (possibly into consoleparser like readInt() -> maybe use inner scanners)
 */

public class Main {
  public static void main(String[] args) {
   Ticket raw = new Ticket();
   TicketBuilder builder = new TicketBuilder(raw);
   TypedBiConsumer<Ticket,String> consumer = new TypedBiConsumer<>(String.class, Ticket::setName);
   builder.attr(consumer, "My Ticket");
   builder.build();
   //  TicketConsole console = new TicketConsole().init();
   //  TicketCollection collection =
   //      new TicketCollection("My Collection", new TicketIdGenerator(), new TicketIdGenerator());
   //  ObjectMapperDecorator mapper = new ObjectMapperDecorator(new XmlMapper());
   //  TicketShell shell = new TicketShell(console, console, new TreeMap<>(), new ArrayList<>());
   //  FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
   //  shell.initDefaultCommands(collection, "TICKET_PATH", fd, mapper, mapper);
   //  shell.open();
  }
}
