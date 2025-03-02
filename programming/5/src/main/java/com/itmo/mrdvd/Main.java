package com.itmo.mrdvd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.collection.TicketIdGenerator;
import com.itmo.mrdvd.device.Console;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.TicketXMLMapper;
import com.itmo.mrdvd.shell.TicketShell;

/*
 * TODO:
 * 1. In UpdateCommand set the max length of printed value of ticket fields;
 * 2. Print fixed length in command description
 */

public class Main {
  public static void main(String[] args) throws JsonProcessingException {
    Console console = new Console().init();
    TicketCollection collection =
        new TicketCollection("My Collection", new TicketIdGenerator(), new TicketIdGenerator());
    FileIO file = new FileIO();
    TicketXMLMapper mapper = new TicketXMLMapper();
    TicketShell shell = new TicketShell(console, console, collection, mapper, mapper, file);
    shell.open();
  }
}
