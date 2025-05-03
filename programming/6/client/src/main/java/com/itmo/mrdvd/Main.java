package com.itmo.mrdvd;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.device.DataConsole;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.EmptyQuery;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.mappers.QueryMapper;
import com.itmo.mrdvd.proxy.response.EmptyResponse;
import com.itmo.mrdvd.queries.UserQuery;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/*
 * TODO:
 * 1. How to do the client-side validation?
 *    - idea: use JavaScript to validate the input
 *    - when sending a query, execute JavaScript files if it has params (for validation purposes)
 * 2. Add [ERROR] / [WARN] prefix of exceptions on Shell level (maybe write my own exceptions with additional info for ERR/WARN differentiation)
 * 3. Realize the client-side validation (inrelevant for now)
 *    - #### SCHEME:
 * ```` 1. Shell gets the list of available Queries from ServerProxy (sends the query via ClientProxy)
 *      2. ClientProxy receives available Queries and validations JavaScript files.
 *      3. Shell validates input via JavaScript files.
 *      4. ClientProxy sends the validated Query to ServerProxy.
 * 4. Somehow split the PublicServerExecutor and PrivateServerExecutor:
 *    - integrate PrivateShell into ServerProxy (but it will be blocking)
 *    - or split the shell and proxy at all (even with composition) and force it to speak with each other via unix sockets
 *    - but then i have to differentiate between net socket and unix socket inside listener
 *
 * 0. Split the app into modules (PLANNED & may be not relevant)
 *    - exit in server vs exit in client; save in server
 *      - maybe split it into exit (shell command) & shutdown (server-side command)
 *    - Proxy as interface (ServerProxy, ClientProxy)
 *      - accepts incoming connections (local or net)
 *      - sends response to the client
 *      - passes queries to executor
 *    - Executor - executes incoming commands
 *      - ServerExecutor vs ClientExecutor which differ only in pack of commands
 *    - Protocol as interface (HttpProtocol)
 *      - parses incoming packets (HTTP or Local stuff - or maybe use unified and no reason to use two protocols - just use different socket on localhost)
 *      - add netcommand which only sends the description of executing command
 *    - ServerResponse - sends response to the client (maybe connect with Protocol which parses the query -> this one will be generating query, and ServerProxy will send generated response)
 * 4. Rewrite updaters & move them from collection to shell commands (see builders)
 *
 * write usedpackages in readme
 */

public class Main {
  public static void main(String[] args) {
    ObjectSerializer<Query> serialQuery = new ObjectSerializer<>(new XmlMapper(), Query.class);
    ObjectSerializer<EmptyResponse> serialResponse =
        new ObjectSerializer<>(new XmlMapper(), EmptyResponse.class);
    ClientSender sender = new ClientSender(serialQuery, serialResponse);
    ClientExecutor exec =
        new ClientExecutor(new FileIO(Path.of(""), FileSystems.getDefault()), sender);
    ClientProxy proxy = new ClientProxy(sender, exec, new QueryMapper(EmptyQuery::new));
    CollectionShell shell = new CollectionShell(proxy, UserQuery::new);
    DataConsole console = new DataConsole().init();
    shell.setTty(new TTY(null, console, console) {});
    shell.start();
  }
}
