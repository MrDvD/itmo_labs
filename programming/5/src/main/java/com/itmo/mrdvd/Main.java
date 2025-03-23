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
import com.itmo.mrdvd.shell.BasicLinkedInput;
import com.itmo.mrdvd.shell.TicketShell;

/*
 * TODO:
 * 1. Check FileIO --- написать в комментариях к FileIO, почему он вообще нужен (по условию лабы)
 * 2. Check execution of scripts
 * 3. Ideally, TicketShell should be splitted from DataShell (create a new class)
 * 4. Ideally, remove Ticket- prefix from generic classes
 * 5.1. Fix a bug with loading collection from file
 * 5.2. Move LoadCommand to the shell
 * 6. Replace Scanner with BufferedInputStream (hasNext is shish)
 */

public class Main {
  public static void main(String[] args) {
    DataConsole console = new DataConsole().init();
    TicketCollection collection = new TicketCollection("My Collection");
    ObjectMapperDecorator<Collection<Ticket,List<Ticket>>> mapper = new ObjectMapperDecorator<>(new XmlMapper(), TicketCollection.class);
    TicketShell shell = new TicketShell(console, new BasicLinkedInput<>(console), new TreeMap<>(), new ArrayList<>());
    FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    shell.initDefaultCommands(collection, "COLLECT_PATH", fd, mapper, mapper);
    shell.open();
  }
}
