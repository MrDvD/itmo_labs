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
 * 1. Remove "commands" from shells --> executors
 * 
 * 1. Create a separate class which is considered a Packet which traverses the net and supplies info about command (type, payload)
 *    - maybe it would be better if i use an http server for this
 * 2. Split the app into modules:
 *    - exit in server vs exit in client; save in server
 *    - Proxy as interface (ServerProxy, ClientProxy)
 *      - accepts incoming connections (local or shared)
 *      - sends response to the client (two separate but parallel classes)
 *      - process queries and execute commands itself
 *    - Executor - executes incoming commands
 *      - ServerExecutor vs ClientExecutor which differ only in pack of commands
 *    - Protocol as interface (HttpProtocol)
 *      - parses incoming packets (HTTP or Local stuff - or maybe use unified and no reason to use two protocols - just use different socket on localhost)
 *      - add netcommand which only sends the description of executing command
 *    - ServerResponse - sends response to the client (maybe connect with Protocol which parses the query -> this one will be generating query, and ServerProxy will send generated response)
 * 3. What does mean "неблокирующий режим"?
 * 4. Move builder from collection to shell commands
 *    - also move updater from collection (make two add/update methods : safe and not safe version w or w/o validation)
 * 5. Create somewhat of FinalTicket (which is created once and has all final fields).
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
