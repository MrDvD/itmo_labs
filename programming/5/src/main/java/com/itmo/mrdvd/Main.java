package com.itmo.mrdvd;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.collection.Collection;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.DataConsole;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.ObjectMapperDecorator;
import com.itmo.mrdvd.object.Ticket;
import com.itmo.mrdvd.shell.TicketShell;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

/*
 * TODO:
 * 1. Check FileIO --- написать в комментариях к FileIO, почему он вообще нужен (по условию лабы)
 * 2. Ideally, TicketShell should be splitted from DataShell (create a new class)
 * 3. Ideally, remove Ticket- prefix from generic classes
 * 5. Move LoadCommand to the shell
 * 6. Replace Scanner with BufferedInputStream (hasNext has strange consequences in shell's interactive behaviour)
 * 7. Check ADD-ing items from file
 * 8. FEATURE: support of Unicode.
 * 9. Попытаться полиморфно обработать комманды -> что, если команда реализует несколько интерфейсов?
 * мб вообще сделать Base интерфейс (isBase метод)
 * Existance of BeanDeserializer
 */

public class Main {
  public static void main(String[] args) {
    DataConsole console = new DataConsole().init();
    TicketCollection collection = new TicketCollection("My Collection");
    ObjectMapperDecorator<Collection<Ticket, List<Ticket>>> mapper =
        new ObjectMapperDecorator<>(new XmlMapper(), TicketCollection.class);
    TicketShell shell = new TicketShell(console, console);
    FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    shell.initDefaultCommands(collection, "COLLECT_PATH", fd, mapper, mapper, new HashSet<>());
    shell.open();
  }
}
