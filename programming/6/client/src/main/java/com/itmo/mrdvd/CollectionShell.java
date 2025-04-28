package com.itmo.mrdvd;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.device.input.InteractiveInputDevice;
import com.itmo.mrdvd.executor.commands.CommandWithParams;
import com.itmo.mrdvd.executor.commands.response.Response;
import com.itmo.mrdvd.executor.commands.shell.ShellCommand;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.ClientProxy;
import com.sun.net.httpserver.Request;

public class CollectionShell extends AbstractShell {
  protected final ClientProxy proxy;
  private boolean isOpen;

  public CollectionShell(ClientProxy proxy, TTY tty) {
    this(proxy, tty, new HashMap<>());
  }

  public CollectionShell(ClientProxy proxy, TTY tty, Map<String, Object> args) {
    super(tty, args);
    this.proxy = proxy;
  }

  /**
   * Processes the passed Response.
   */
  protected void processResponse(Response r) {

  }

  /**
   * Passes the Request to proxy & returns its Response. 
   */
  protected Response processRequest(Request q) {
    Response r = this.proxy.send(q);
  }

  /**
   * Wraps the user command into Request.
   */
  protected Request createRequest(String cmd) {
    // here i should get args related to cmd (if present)
  }

  protected void processLine() throws IOException {
    Optional<String> cmdName = getTty().getIn().readToken();
    if (cmdName.isEmpty()) {
      getTty().getIn().skipLine();
      return;
    }
    // Optional<ShellCommand> c = getCommand(cmdName.get());
    // if (c.isPresent()) {
    //   if (!c.get().hasArgs()) {
    //     getIn().skipLine();
    //   }
    //   try {
    //     c.get().execute();
    //   } catch (RuntimeException e) {
    //     this.getOut().writeln(e.getMessage());
    //   }
    //   return Optional.empty();
    // }
    // Optional<Query> q = getQuery(cmdName.get());
    // if (q.isPresent()) {
    //   Optional<ShellCommand> rawProcess = getCommand("process_query");
    //   if (rawProcess.isPresent() && rawProcess.get() instanceof CommandWithParams processQuery) {
    //     try {
    //       processQuery.withParams(List.of(q.get())).execute();
    //     } catch (RuntimeException e) {
    //       this.getOut().writeln(e.getMessage());
    //     }
    //   } else {
    //     this.getOut().writeln("В интерпретаторе не обнаружен обработчик запросов.");
    //   }
    //   return Optional.empty();
    // }
    // return cmdName;
  }

  @Override
  public void start() {
    setArg("exit", this);
    this.isOpen = true;
    while (this.isOpen) {
      if (getTty().getIn() instanceof InteractiveInputDevice back) {
        back.write("> ");
      }
      while (!getTty().getIn().hasNext()) {
        getTty().getIn().closeIn();
        stop();
        return;
      }
      // execute here wrapper & use getArg()
      // send via proxy
      // wait for response
      // print it
    }
  }

  @Override
  public void stop() {
    this.isOpen = false;
  }
}
