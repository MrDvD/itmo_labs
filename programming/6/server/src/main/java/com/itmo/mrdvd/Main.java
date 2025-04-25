package com.itmo.mrdvd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.List;

import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.impl.io.DefaultClassicHttpRequestFactory;
import org.apache.hc.core5.http.impl.io.DefaultHttpRequestParser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itmo.mrdvd.builder.builders.SuppliedCoordinatesBuilder;
import com.itmo.mrdvd.builder.builders.SuppliedEventBuilder;
import com.itmo.mrdvd.builder.builders.SuppliedTicketBuilder;
import com.itmo.mrdvd.collection.TicketCollection;
import com.itmo.mrdvd.device.ObjectMapperDecorator;
import com.itmo.mrdvd.executor.queries.Query;
import com.itmo.mrdvd.proxy.HttpProtocol;

public class Main {
  public static void main(String[] args) throws IOException {
    HttpProtocol http =
        new HttpProtocol(new DefaultHttpRequestParser(), new DefaultClassicHttpRequestFactory());
    ObjectMapperDecorator mapper =
        new ObjectMapperDecorator(new XmlMapper(), ContentType.APPLICATION_XML);
    http.addSerializationPair(mapper, mapper);
    TicketCollection collect = new TicketCollection();
    SuppliedTicketBuilder ticketBuild = new SuppliedTicketBuilder(new SuppliedEventBuilder(), new SuppliedCoordinatesBuilder());
    ServerExecutor executor = new ServerExecutor(collect, ticketBuild);
    // подумать над возвращением результата, потому что сейчас его нет
    CollectionServerProxy proxy = new CollectionServerProxy(Selector.open(), http, (Query q) -> { return executor.processQuery(q, List.of()); });
    ServerSocketChannel socket = ServerSocketChannel.open();
    socket.bind(new InetSocketAddress("localhost", 8080));
    socket.configureBlocking(false);
    proxy.addListener(socket);
    proxy.listen();
    // DataConsole console = new DataConsole().init();
    // FileIO fd = new FileIO(Path.of(""), FileSystems.getDefault());
    // CollectionShell shell = new CollectionShell(executor, proxy, console, console, fd);
    // shell.open();
  }
}
