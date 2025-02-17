package com.itmo.mrdvd;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.Console;
import com.itmo.mrdvd.shell.TicketShell;

public class Main {
  public static void main(String[] args) {
    Console console = new Console();
    TicketCollection collection = new TicketCollection();
    TicketShell shell = new TicketShell(console, console, collection);
    // console.write("555\n");
    shell.open();
  }
}
