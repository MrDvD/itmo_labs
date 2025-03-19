package com.itmo.mrdvd;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.DataConsole;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.ObjectMapperDecorator;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.shell.TicketShell;

/*
 * TODO:
 * 1. Update UpdateCommand (with Builder) https://tinyurl.com/mvpjps93:
 *    - update(Object obj): provides raw object with prefilled fields to update
 *    - check*(obj::getterMethod): which field to check
 *    - this all is inherited by UpdatableBuilder
 *    - make interfaces for builder, updatablebuilder, interactivebuilder
 *    - remove rawobject in ordinary builder (pass ::new instead)
 *    - BUG: Builder should be reusable (bc it initialized once in main)
 * 2. Check FileIO
 * 3. Check execution of scripts
 * 4. Try to redirect commands from executeScript to shell's inputdevice
 *    - check out System.setIn() method
 *    - check out Obsidian's notes and screenshots
 * 5. Ideally, TicketShell should be splitted from DataShell (create a new class)
 * 6. Ideallym rename unrelated classes without Ticket prefix
 */

public class Main {
  public static void main(String[] args) {
    DataConsole console = new DataConsole().init();
    TicketCollection collection = new TicketCollection("My Collection");
    ObjectMapperDecorator<Collection<Ticket,List<Ticket>>> mapper = new ObjectMapperDecorator<>(new XmlMapper(), TicketCollection.class);
    TicketShell shell = new TicketShell(console, console, new TreeMap<>(), new ArrayList<>());
    FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    shell.initDefaultCommands(collection, "TICKET_PATH", fd, mapper, mapper);
    shell.open();
  }
}
