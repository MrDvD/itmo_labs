package com.itmo.mrdvd;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.builders.InteractiveCoordinatesBuilder;
import com.itmo.mrdvd.builders.InteractiveEventBuilder;
import com.itmo.mrdvd.builders.InteractiveTicketBuilder;
import com.itmo.mrdvd.device.DataConsole;
import com.itmo.mrdvd.device.FileIO;
import com.itmo.mrdvd.device.TTY;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.ObjectDeserializer;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.response.EmptyResponse;
import com.itmo.mrdvd.queries.UserQuery;
import com.itmo.mrdvd.updaters.InteractiveCoordinatesUpdater;
import com.itmo.mrdvd.updaters.InteractiveEventUpdater;
import com.itmo.mrdvd.updaters.InteractiveTicketUpdater;

/*
 * 1. Split Update query into two parts:
 *    - check existance of object with the following id (and get it)
 *    - update retrieved object and send it to the server 
 */
public class Main {
  public static void main(String[] args) {
    ObjectSerializer<Query> serialQuery = new ObjectSerializer<>(new XmlMapper());
    ObjectDeserializer<EmptyResponse> deserialResponse =
        new ObjectDeserializer<>(new XmlMapper(), EmptyResponse.class);
    ClientSender sender = new ClientSender(serialQuery, deserialResponse);
    ClientExecutor exec =
        new ClientExecutor(new FileIO(Path.of(""), FileSystems.getDefault()), sender);
    ClientProxy proxy = new ClientProxy(sender, exec);
    CollectionShell shell = new CollectionShell(proxy, UserQuery::new);
    shell.setBuilders(
        new InteractiveTicketBuilder(
            new InteractiveCoordinatesBuilder(shell), new InteractiveEventBuilder(shell), shell),
        new InteractiveTicketUpdater(
            new InteractiveCoordinatesUpdater(shell), new InteractiveEventUpdater(shell), shell));
    DataConsole console = new DataConsole().init();
    shell.setTty(new TTY(null, console, console) {});
    shell.start();
  }
}
