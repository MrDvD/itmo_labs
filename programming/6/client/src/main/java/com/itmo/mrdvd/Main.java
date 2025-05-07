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
import com.itmo.mrdvd.proxy.EmptyQuery;
import com.itmo.mrdvd.proxy.Query;
import com.itmo.mrdvd.proxy.mappers.ObjectSerializer;
import com.itmo.mrdvd.proxy.mappers.QueryMapper;
import com.itmo.mrdvd.proxy.response.EmptyResponse;
import com.itmo.mrdvd.queries.UserQuery;

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
    shell.setBuilder(new InteractiveTicketBuilder(new InteractiveCoordinatesBuilder(shell), new InteractiveEventBuilder(shell), shell));
    DataConsole console = new DataConsole().init();
    shell.setTty(new TTY(null, console, console) {});
    shell.start();
  }
}
