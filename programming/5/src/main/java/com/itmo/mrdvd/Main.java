package com.itmo.mrdvd;

import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.collection.TicketIdGenerator;
import com.itmo.mrdvd.device.Console;
import com.itmo.mrdvd.shell.TicketShell;

/*
 * TODO:
 * 1. In UpdateCommand set the max length of printed value of ticket fields;
 * 2. Print fixed length in command description
 */

public class Main {
  public static void main(String[] args) {
    Console console = new Console();
    TicketCollection collection =
        new TicketCollection(new TicketIdGenerator(), new TicketIdGenerator());
    TicketShell shell = new TicketShell(console, console, collection);
    shell.open();
  }
}
