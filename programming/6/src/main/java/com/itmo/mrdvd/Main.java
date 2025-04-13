package com.itmo.mrdvd;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Value;

/*
 * TODO:
 * 1. Remove "commands" from shells --> executors
 *    - instead, add queries
 *    - list of queries shouldn't be hard-coded: it should automatically generate from current executor's commands
 *    - add command to terminal which loads available queries in the beginning
 *    - input queries should be validated in terminal
 *    - #### SCHEME:
 *      1. Shell sends to Proxy AskingQuery if it has the command with the following name.
 *      2. Proxy sends to Shell the Query description (params, validation, etc)
 *      3. Shell validates the Query
 *      4. If everything is OK, Shell sends the CommandQuery to Proxy
 * 3. How to do the client-side validation?
 *    - idea: use JavaScript to validate the input
 * 
 * study selectors!!!
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
 *
 * write usedpackages in readme
 */

public class Main {
  public static void main(String[] args) {
    String JS_CODE = "(function myFun(param){console.log('Hello ' + param + ' from JS');})";
    String who = args.length == 0 ? "World" : args[0];
    Engine engine = Engine.newBuilder().option("engine.WarnInterpreterOnly", "false").build();
    Context ctx = Context.newBuilder("js").engine(engine).build();
    Value value = ctx.eval("js", JS_CODE);
    value.execute(who);

    // DataConsole console = new DataConsole().init();
    // TicketCollection collection = new TicketCollection("My Collection");
    // ObjectMapperDecorator<Collection<Ticket, List<Ticket>>> mapper =
    //     new ObjectMapperDecorator<>(new XmlMapper(), TicketCollection.class);
    // TicketShell shell = new TicketShell(console, console);
    // FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    // shell.initDefaultCommands(collection, "COLLECT_PATH", fd, mapper, mapper, new HashSet<>());
    // shell.open();
  }
}
