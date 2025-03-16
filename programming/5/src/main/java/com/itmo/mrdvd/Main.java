package com.itmo.mrdvd;

import java.time.LocalDateTime;

import com.itmo.mrdvd.builder.CoordinatesBuilder;
import com.itmo.mrdvd.builder.EventBuilder;
import com.itmo.mrdvd.builder.TicketBuilder;
import com.itmo.mrdvd.device.TicketConsole;

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
   TicketConsole console = new TicketConsole().init();
   CoordinatesBuilder coordBuilder = new CoordinatesBuilder(console, console);
   EventBuilder eventBuilder = new EventBuilder(console, console, console);
   TicketBuilder ticketBuilder = new TicketBuilder(coordBuilder, eventBuilder, console, console, console, LocalDateTime.now(), console);
   ticketBuilder.interactiveBuild();
   //  TicketCollection collection =
   //      new TicketCollection("My Collection", new TicketIdGenerator(), new TicketIdGenerator());
   //  ObjectMapperDecorator mapper = new ObjectMapperDecorator(new XmlMapper());
   //  TicketShell shell = new TicketShell(console, console, new TreeMap<>(), new ArrayList<>());
   //  FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
   //  shell.initDefaultCommands(collection, "TICKET_PATH", fd, mapper, mapper);
   //  shell.open();
  }
}
