package com.itmo.mrdvd;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.collection.TicketIdGenerator;
import com.itmo.mrdvd.device.Console;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.ObjectMapperDecorator;
import com.itmo.mrdvd.shell.TicketShell;

/*
 * TODO:
 * 1. Add BuilderObject in AddCommand as passed param
 * 2. Raise Exceptions instead of returning an int.
 * 3. Update UpdateCommand (with Optionals)
 */

public class Main {
  public static void main(String[] args) {
    Console console = new Console().init();
    TicketCollection collection =
        new TicketCollection("My Collection", new TicketIdGenerator(), new TicketIdGenerator());
    ObjectMapperDecorator mapper = new ObjectMapperDecorator(new XmlMapper());
    TicketShell shell = new TicketShell(console, console, new TreeMap<>(), new ArrayList<>());
    FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    shell.initDefaultCommands(collection, "TICKET_PATH", fd, mapper, mapper);
    shell.open();
  }
}
