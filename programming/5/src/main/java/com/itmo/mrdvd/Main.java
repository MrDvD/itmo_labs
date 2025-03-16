package com.itmo.mrdvd;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.ObjectMapperDecorator;
import com.itmo.mrdvd.device.TicketConsole;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.shell.TicketShell;

/*
 * TODO:
 * 1. Update UpdateCommand (with Optionals)
 * 2. Check FileIO
 * 3. Check execution of scripts
 */

public class Main {
  public static void main(String[] args) {
    TicketConsole console = new TicketConsole().init();
    TicketCollection collection = new TicketCollection("My Collection");
    ObjectMapperDecorator<Collection<Ticket,List<Ticket>>> mapper = new ObjectMapperDecorator<>(new XmlMapper(), TicketCollection.class);
    TicketShell shell = new TicketShell(console, console, new TreeMap<>(), new ArrayList<>());
    FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    shell.initDefaultCommands(collection, "TICKET_PATH", fd, mapper, mapper);
    shell.open();
  }
}
