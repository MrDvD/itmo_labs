package com.itmo.mrdvd;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.ObjectMapperDecorator;
import com.itmo.mrdvd.device.TicketConsole;
import com.itmo.mrdvd.shell.TicketShell;

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
    TicketCollection collection = new TicketCollection("My Collection");
    ObjectMapperDecorator mapper = new ObjectMapperDecorator(new XmlMapper());
    TicketShell shell = new TicketShell(console, console, new TreeMap<>(), new ArrayList<>());
    FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    shell.initDefaultCommands(collection, "TICKET_PATH", fd, mapper, mapper);
    shell.open();
  }
}
